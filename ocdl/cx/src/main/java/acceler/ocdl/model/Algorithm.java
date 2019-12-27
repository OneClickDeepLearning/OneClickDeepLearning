package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.InitStorageException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.utils.SerializationUtils;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;


public class Algorithm extends Storable implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Algorithm.class);

    private static List<Algorithm> algorithmStorage;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private static List<Algorithm> getAlgorithmStorage() {
        if (algorithmStorage == null) {
            logger.error("AlgorithmStorage instance is null");
            throw new InitStorageException("AlgorithmStorage instance is null");
        }

        return algorithmStorage;
    }

    static void initializeStorage(String dataPath) {
        if (algorithmStorage == null) {
            logger.info("[init] AlgorithmStorage instance initialization executed");
            File algorithmDataFile = new File(Paths.get(dataPath, CONSTANTS.PERSISTENCE.ALGORITHMS).toString());
            try {
                algorithmStorage = (ArrayList) StorageLoader.loadStorage(algorithmDataFile);
            } catch (NotFoundException nfe) {
                algorithmStorage = new ArrayList<>();
            }
        }

        logger.warn("Storage initialization only allow been executed at init time");
    }

    private static void persistence() {
        File dumpFile = new File(Paths.get(Project.getDataPathInStorage(), CONSTANTS.PERSISTENCE.ALGORITHMS).toString());
        SerializationUtils.dump(algorithmStorage, dumpFile);
    }

    public static boolean existApprovalModel(ApprovedModel model) {
        boolean result = false;

        lock.readLock().lock();
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getModelId().equals(model.getModelId())) {
                    result = true;
                    break;
                }
            }
        }
        lock.readLock().unlock();

        return result;
    }

    public static Algorithm getAlgorithmOfApprovedModel(ApprovedModel model) throws NotFoundException {
        Algorithm target = getRealAlgorithmOfModel(model);

        if (target == null) {
            throw new NotFoundException("approved model not found:" + model.getName());
        }

        return target.deepCopy();
    }

    private static Algorithm getRealAlgorithmOfModel(ApprovedModel model) {
        Algorithm target = null;

        lock.readLock().lock();
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getModelId().equals(model.getModelId())) {
                    target = algorithm;
                }
            }
        }
        lock.readLock().unlock();

        return target;
    }

    public static void updateSingleApprovedModel(ApprovedModel model, Consumer<ApprovedModel> consumer) {

        System.out.println("enter the update single approved model");
        lock.writeLock().lock();
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getModelId().equals(model.getModelId())) {
                    System.out.println("find the model");
                    consumer.accept(m);
                    break;
                }
            }
        }
        lock.writeLock().unlock();
        persistence();
    }

    public static Optional<ApprovedModel> getApprovalModelById(Long modelId) {
        ApprovedModel target = null;

        lock.readLock().lock();
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getModelId().equals(modelId)) {
                    target = m.deepCopy();
                    break;
                }
            }
        }
        lock.readLock().unlock();

        return Optional.ofNullable(target);
    }

    public static Algorithm[] getAlgorithms() {
        lock.readLock().lock();
        Algorithm[] algorithms = getAlgorithmStorage().stream().map(Algorithm::deepCopy).toArray(size -> new Algorithm[size]);
        lock.readLock().unlock();

        return algorithms;
    }

    public static Optional<Algorithm> getAlgorithmByName(String algorithmName) {

        Optional<Algorithm> algorithmOpt = getRealAlgorithmByName(algorithmName);

        Algorithm copy = algorithmOpt.map(Algorithm::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean existAlgorithm(String algorithmName) {
        return getRealAlgorithmByName(algorithmName).isPresent();
    }

    public static void addNewAlgorithm(Algorithm newAlgorithm) {
        lock.writeLock().lock();
        getAlgorithmStorage().add(newAlgorithm.deepCopy());
        persistence();
        lock.writeLock().unlock();
    }

    public static Algorithm removeAlgorithm(String algorithmName) throws NotFoundException {
        lock.writeLock().lock();
        Optional<Algorithm> targetOpt = getAlgorithmStorage().stream().filter(algorithm -> (algorithm.getAlgorithmName().equals(algorithmName))).findFirst();
        targetOpt.ifPresent(getAlgorithmStorage()::remove);
        persistence();
        lock.writeLock().unlock();

        return targetOpt.orElseThrow(() -> (new NotFoundException("algorithm not found")));
    }

    /**
     * @return Map<algorithmName, belongingApprovedModels>
     */
    public static Map<String, Model[]> getAllAlgorithmAndModels() {
        Map<String, Model[]> result = new HashMap<>();

        lock.readLock().lock();
        for (Algorithm algorithm : getAlgorithmStorage()) {
            Model[] belongingModelCopies = new Model[algorithm.belongingModels.size()];

            for (int i = 0; i < algorithm.belongingModels.size(); i++) {
                belongingModelCopies[i] = algorithm.belongingModels.get(i).deepCopy();
            }


            result.put(algorithm.getAlgorithmName(), belongingModelCopies);
        }
        lock.readLock().unlock();

        return result;
    }

    private static Optional<Algorithm> getRealAlgorithmByName(String algorithmName) {
        lock.readLock().lock();
        Optional<Algorithm> algorithmOpt = getAlgorithmStorage().stream().filter(a -> a.algorithmName.equals(algorithmName)).findFirst();
        lock.readLock().unlock();

        return algorithmOpt;
    }

    public static void removeApprovedModelFromAlgorithm(String algorithmName, ApprovedModel model) throws NotFoundException {
        Optional<Algorithm> algorithmOpt = getRealAlgorithmByName(algorithmName);

        if (!algorithmOpt.isPresent()) {
            throw new NotFoundException("algorithm not found");
        }

        lock.writeLock().lock();

        algorithmOpt.get().belongingModels.stream()
                .filter(m -> (m.getModelId().equals(model.getModelId())))
                .findFirst()
                .ifPresent(targetModel -> algorithmOpt.get().belongingModels.remove(targetModel));

        lock.writeLock().unlock();
        persistence();
    }


//    private AtomicLong releaseVersionGenerator;
//    private AtomicLong cachedVersionGenerator;

    private String algorithmName;
    private Long currentReleasedVersion;
    private Long currentCachedVersion;
    private List<ApprovedModel> belongingModels;


    public Algorithm() {
        this.belongingModels = new ArrayList<>();
//        this.releaseVersionGenerator = new AtomicLong(0);
//        this.cachedVersionGenerator = new AtomicLong(0);
    }

    public Algorithm deepCopy() {
        Algorithm copy = new Algorithm();
        copy.algorithmName = this.algorithmName;

        copy.currentReleasedVersion = this.currentReleasedVersion;
        copy.currentCachedVersion = this.currentCachedVersion;

        copy.belongingModels = Lists.newArrayList(getBelongingModelsCopies());

        return copy;
    }

    private ApprovedModel[] getBelongingModelsCopies() {
        return this.belongingModels.stream().map(ApprovedModel::deepCopy).toArray(size -> new ApprovedModel[size]);
    }

    public ApprovedModel approveModel(NewModel model, UpgradeVersion version, String comments, Long lastOperatorId) {

        AtomicLong releaseVersionGenerator = this.currentReleasedVersion != null? new AtomicLong(this.currentReleasedVersion): new AtomicLong(0L);
        AtomicLong cachedVersionGenerator = this.currentCachedVersion != null? new AtomicLong(this.currentCachedVersion) : new AtomicLong(0L);

        Long newReleasedVersion = 0L;
        Long newCachedVersion = 0L;

        if (version == UpgradeVersion.RELEASE_VERSION) {
            newReleasedVersion = releaseVersionGenerator.incrementAndGet();
            newCachedVersion = 0L;
        } else {
            newCachedVersion = cachedVersionGenerator.incrementAndGet();
            newReleasedVersion = releaseVersionGenerator.get();
        }

        return model.convertToApprovedModel(newCachedVersion, newReleasedVersion, comments, lastOperatorId);
    }

    public void persistAlgorithmVersion(ApprovedModel model) {

        Optional<Algorithm> realAlgorithm = getRealAlgorithmByName(this.algorithmName);
        realAlgorithm.ifPresent(algorithm -> {
            algorithm.setCurrentReleasedVersion(model.getReleasedVersion());
            algorithm.setCurrentCachedVersion(model.getCachedVersion());
        });
        Algorithm.persistence();
    }

    public void persistApprovalModel(ApprovedModel model) {
        if (model.getReleasedVersion() == null || model.getCachedVersion() == null) {
            logger.error("Can not persist the model that misses cachedVersion or releasedVersion:" + model.getName());
            throw new OcdlException("Can not persist the model that misses achedVersion or releasedVersion");
        }

        if (containsModel(model)) {
            throw new ExistedException("model already existed in algorithm");
        }

        ApprovedModel copyOfModel = model.deepCopy(); //avoid any ref outside
        logger.debug("befor add to algorithm: " + copyOfModel.getOwnerId());
        lock.writeLock().lock();
        Optional<Algorithm> algorithmOpt = getRealAlgorithmByName(this.algorithmName);
        algorithmOpt.ifPresent(algorithm -> {
                    algorithm.belongingModels.add(copyOfModel);
        });
        persistence();
        lock.writeLock().unlock();
    }

    public boolean containsModel(Model model) {
        return getRealAlgorithmByName(this.algorithmName).map(algorithm -> algorithm.belongingModels.stream().anyMatch(m -> m.getModelId().equals(model.getModelId()))).orElse(false);
    }

    public String getAlgorithmName() {
        return this.algorithmName;
    }

    public Long getCurrentReleasedVersion() {
        return this.currentReleasedVersion;
    }

    public Long getCurrentCachedVersion() {
        return this.currentCachedVersion;
    }

    public List<ApprovedModel> getBelongingModels() {
        return belongingModels;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setCurrentReleasedVersion(Long currentReleasedVersion) {
        this.currentReleasedVersion = currentReleasedVersion;
    }

    public void setCurrentCachedVersion(Long currentCachedVersion) {
        this.currentCachedVersion = currentCachedVersion;
    }

    public void setBelongingModels(List<ApprovedModel> belongingModels) {
        this.belongingModels = belongingModels;
    }

    public enum UpgradeVersion {
        RELEASE_VERSION,
        CACHED_VERSION
    }
}
