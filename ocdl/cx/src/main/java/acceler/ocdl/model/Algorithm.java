package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.utils.SerializationUtils;

import java.io.File;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Algorithm implements Serializable {
    private static final long serialVersionUID = -2767605614048989439L;

    private static final List<Algorithm> algorithmStorage = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

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

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    /**
     * To protect dirty model data in memory, only expose copy of these model
     */
    public ApprovedModel[] getBelongingModels() {
        return (ApprovedModel[]) this.belongingModels.stream().map(ApprovedModel::deepCopy).toArray();
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
            return;
        }

        if (containsModel(model)) {
            throw new ExistedException();
        }

        lock.writeLock().lock();
        this.belongingModels.add(model);
        lock.writeLock().unlock();
    }

    /**
     * get copy of approved model from algorithm's belongingModels by name
     */
    public Optional<ApprovedModel> getApprovedModelByName(String name) {
        Optional<ApprovedModel> modelOpt = getRealModelByName(name);
        ApprovedModel copy = modelOpt.map(ApprovedModel::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean approvalModelExist(ApprovedModel model) {
        for (Algorithm algorithm : algorithmStorage) {
            for (ApprovedModel m : algorithm.getBelongingModels()) {
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
            throw new NotFoundException("algorithm not found:" + model.getName(), "Algorithm Not Found");
        }

        return target.deepCopy();
    }

    public void removeApprovedModelFromAlgorithm(ApprovedModel model) {
        Algorithm algorithmOfModel = getRealAlgorithmOfModel(model);
        Optional<ApprovedModel> targetOpt = algorithmOfModel.belongingModels.stream().filter(m -> (
                m.getName().equals(model.getName()) && m.getApprovedTime().equals(model.getApprovedTime())
        )).findFirst();

        targetOpt.ifPresent(approvedModel -> algorithmOfModel.belongingModels.remove(approvedModel));

        persistence();
    }

    private static Algorithm getRealAlgorithmOfModel(ApprovedModel model) {
        for (Algorithm algorithm : algorithmStorage) {
            for (ApprovedModel m : algorithm.getBelongingModels()) {
                if (m.getName().equals(model.getName()) && m.getApprovedTime().equals(model.getApprovedTime())) {
                    return algorithm;
                }
            }
        }
        return null;
    }

    private static void persistence() {
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTENCE.ALGORITHMS);
        SerializationUtils.dump(algorithmStorage, dumpFile);
        lock.writeLock().unlock();
    }

    public static Optional<ApprovedModel> getApprovalModelByName(String modelName) {
        ApprovedModel target = null;
        for (Algorithm algorithm : algorithmStorage) {
            for (ApprovedModel m : algorithm.belongingModels) {
                if (m.getName().equals(modelName)) {
                    target = m;
                    break;
                }
            }
        }

        return Optional.ofNullable(target);
    }


    public boolean containsModel(Model model) {
        return getRealModelByName(model.name).isPresent();
    }


    public Algorithm deepCopy() {
        Algorithm copy = new Algorithm();
        copy.algorithmName = this.algorithmName;
        copy.currentReleasedVersion = this.currentReleasedVersion;
        copy.currentCachedVersion = this.currentCachedVersion;
        copy.belongingModels = Arrays.asList(getBelongingModels());

        return copy;
    }

    public static Algorithm[] getAlgorithms() {
        lock.readLock().lock();
        Algorithm[] algorithms = (Algorithm[]) algorithmStorage.stream().map(Algorithm::deepCopy).toArray();
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
        algorithmStorage.add(newAlgorithm);

        persistence();
    }

    public static Algorithm removeAlgorithm(String algorithmName) throws NotFoundException {
        Optional<Algorithm> targetOpt = algorithmStorage.stream().filter(algorithm -> (algorithm.getAlgorithmName().equals(algorithmName))).findFirst();
        targetOpt.ifPresent(algorithmStorage::remove);

        persistence();

        return targetOpt.orElseThrow(() -> (new NotFoundException("", "")));
    }

    /**
     * warning: return real object of real-time data
     */
    private Optional<ApprovedModel> getRealModelByName(String name) {
        lock.readLock().lock();
        Optional<ApprovedModel> modelOpt = this.belongingModels.stream().filter(am -> (am.name.equals(name))).findFirst();
        lock.readLock().unlock();

        return modelOpt;
    }

    /**
     *
     * @return Map<algorithmName, belongingApprovedModels>
     */
    public static Map<String, Model[]> getAllAlgorithmAndModels() {
        Map<String, Model[]> approvedModels = new HashMap<>();

        for (Algorithm algorithm : algorithmStorage) {
            approvedModels.put(algorithm.getAlgorithmName(), algorithm.getBelongingModels());
        }

        return approvedModels;
    }

    /**
     * warning: return real object of real-time data
     */
    private static Optional<Algorithm> getRealAlgorithmByName(String algorithmName) {
        lock.readLock().lock();
        Optional<Algorithm> algorithmOpt = algorithmStorage.stream().filter(a -> a.algorithmName.equals(algorithmName)).findFirst();
        lock.readLock().lock();

        return algorithmOpt;
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

    public enum UpgradeVersion {
        RELEASE_VERSION,
        CACHED_VERSION
    }
}
