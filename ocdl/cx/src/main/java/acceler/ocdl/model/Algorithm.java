package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.InitStorageException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Algorithm extends Storable implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Algorithm.class);

    private static List<Algorithm> algorithmStorage = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    private static List<Algorithm> getAlgorithmStorage() {
        if (algorithmStorage == null) {
            logger.error("AlgorithmStorage instance is null");
            throw new InitStorageException("AlgorithmStorage instance is null");
        }

        return algorithmStorage;
    }

    static void initializeStorage() {
        if (algorithmStorage == null) {
            logger.info("[init] AlgorithmStorage instance initialization executed");
            File algorithmDataFile = new File(CONSTANTS.PERSISTENCE.ALGORITHMS);
            try {
                algorithmStorage = (ArrayList) StorageLoader.loadStorage(algorithmDataFile);
            } catch (NotFoundException nfe) {
                algorithmStorage = new ArrayList<>();
            }
        }

        logger.warn("Storage initialization only allow been executed at init time");
    }

    private static void persistence() {
        File dumpFile = new File(CONSTANTS.PERSISTENCE.ALGORITHMS);
        SerializationUtils.dump(algorithmStorage, dumpFile);
    }

    public static boolean existApprovalModel(ApprovedModel model) {
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getName().equals(model.getName()) && m.getApprovedTime().equals(model.getApprovedTime())) {
                    return true;
                }
            }
        }

        return false;
    }

    public static Algorithm getAlgorithmOfApprovedModel(ApprovedModel model) throws NotFoundException {
        Algorithm target = getRealAlgorithmOfModel(model);

        if (target == null) {
            throw new NotFoundException("approved model not found:" + model.getName(), "ApprovedModel Not Found");
        }

        return target.deepCopy();
    }

    private static Algorithm getRealAlgorithmOfModel(ApprovedModel model) {
        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getName().equals(model.getName()) && m.getApprovedTime().equals(model.getApprovedTime())) {
                    return algorithm;
                }
            }
        }

        return null;
    }

    public static Optional<ApprovedModel> getApprovalModelByName(String modelName) {
        ApprovedModel target = null;

        for (Algorithm algorithm : getAlgorithmStorage()) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getName().equals(modelName)) {
                    target = m.deepCopy();
                    break;
                }
            }
        }

        return Optional.ofNullable(target);
    }

    public static Algorithm[] getAlgorithms() {
        return getAlgorithmStorage().stream().map(Algorithm::deepCopy).toArray(size -> new Algorithm[size]);
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

        return targetOpt.orElseThrow(() -> (new NotFoundException("algorithm not found", "algorithm not found")));
    }

    /**
     * @return Map<algorithmName, belongingApprovedModels>
     */
    public static Map<String, Model[]> getAllAlgorithmAndModels() {
        Map<String, Model[]> result = new HashMap<>();

        for (Algorithm algorithm : getAlgorithmStorage()) {
            Model[] belongingModelCopies = new Model[algorithm.belongingModels.size()];

            for (int i = 0; i < algorithm.belongingModels.size(); i++) {
                belongingModelCopies[i] = algorithm.belongingModels.get(i).deepCopy();
            }

            result.put(algorithm.getAlgorithmName(), belongingModelCopies);
        }

        return result;
    }

    private static Optional<Algorithm> getRealAlgorithmByName(String algorithmName) {
        lock.readLock().lock();
        Optional<Algorithm> algorithmOpt = getAlgorithmStorage().stream().filter(a -> a.algorithmName.equals(algorithmName)).findFirst();
        lock.readLock().lock();

        return algorithmOpt;
    }

    public static void removeApprovedModelFromAlgorithm(String algorithmName, ApprovedModel model) throws NotFoundException {
        Optional<Algorithm> algorithmOpt = getRealAlgorithmByName(algorithmName);

        if (!algorithmOpt.isPresent()) {
            throw new NotFoundException("algorithm not found", "algorithm not found");
        }

        lock.writeLock().lock();

        algorithmOpt.get().belongingModels.stream()
                .filter(m -> (m.getName().equals(model.getName())))
                .filter(m -> m.getApprovedTime().equals(model.getApprovedTime()))
                .findFirst()
                .ifPresent(targetModel -> algorithmOpt.get().belongingModels.remove(targetModel));

        lock.writeLock().unlock();
        persistence();
    }


    private final AtomicLong releaseVersionGenerator;
    private final AtomicLong cachedVersionGenerator;

    private String algorithmName;
    private Long currentReleasedVersion;
    private Long currentCachedVersion;
    private List<ApprovedModel> belongingModels;


    public Algorithm() {
        this.belongingModels = new ArrayList<>();
        this.releaseVersionGenerator = new AtomicLong(0);
        this.cachedVersionGenerator = new AtomicLong(0);
    }

    public Algorithm deepCopy() {
        Algorithm copy = new Algorithm();
        copy.algorithmName = this.algorithmName;
        copy.currentReleasedVersion = this.currentReleasedVersion;
        copy.currentCachedVersion = this.currentCachedVersion;
        copy.belongingModels = Arrays.asList(getBelongingModelsCopies());

        return copy;
    }

    private ApprovedModel[] getBelongingModelsCopies() {
        return this.belongingModels.stream().map(ApprovedModel::deepCopy).toArray(size -> new ApprovedModel[size]);
    }

    public ApprovedModel approveModel(NewModel model, UpgradeVersion version) {
        if (version == UpgradeVersion.RELEASE_VERSION) {
            this.currentReleasedVersion = this.releaseVersionGenerator.getAndIncrement();
        } else {
            this.currentCachedVersion = this.cachedVersionGenerator.getAndIncrement();
        }

        return model.convertToApprovedModel(this.currentCachedVersion, this.currentReleasedVersion);
    }

    public void persistApprovalModel(ApprovedModel model) {
        if (model.getReleasedVersion() == null || model.getCachedVersion() == null) {
            logger.error("Can not persist the model that misses achedVersion or releasedVersion:" + model.getName());
            throw new OcdlException("Can not persist the model that misses achedVersion or releasedVersion");
        }

        if (containsModel(model)) {
            throw new ExistedException();
        }

        ApprovedModel copyOfModel = model.deepCopy(); //avoid any ref outside

        lock.writeLock().lock();
        getRealAlgorithmByName(this.algorithmName).ifPresent(algorithm -> algorithm.belongingModels.add(copyOfModel));
        persistence();
        lock.writeLock().unlock();
    }

    public boolean containsModel(Model model) {
        return getRealAlgorithmByName(this.algorithmName).map(algorithm -> algorithm.belongingModels.stream().anyMatch(m -> m.name.equals(model.name))).orElse(false);
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
