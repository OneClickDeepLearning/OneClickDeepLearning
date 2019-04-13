package acceler.ocdl.model;

import acceler.ocdl.exception.ExistedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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


    /**
     * To protect dirty model data in memory, only expose copy of these model
     */
    public ApprovedModel[] getBelongingModels() {
        return (ApprovedModel[]) this.belongingModels.stream().map(ApprovedModel::deepCopy).toArray();
    }

    /**
     * one approved model for whatever algorithm is added, whole object will be synchronized with persistence.
     */
    public void approveModel(NewModel model, UpgradeVersion version) {
        if (containsModel(model)) {
            throw new ExistedException();
        }

        if (version == UpgradeVersion.RELEASE_VERSION) {
            this.currentReleasedVersion = this.releaseVersionGenerator.getAndIncrement();
        } else {
            this.currentCachedVersion = this.cachedVersionGenerator.getAndIncrement();
        }

        lock.writeLock().lock();
        ApprovedModel approvedModel = model.convertToApprovedModel(this.currentCachedVersion, this.currentReleasedVersion);
        this.belongingModels.add(approvedModel);
        //TODO: write to file
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
        return (Algorithm[]) algorithmStorage.stream().map(Algorithm::deepCopy).toArray();
    }

    public static Optional<Algorithm> getAlgorithmByName(String algorithmName) {
        Optional<Algorithm> algorithmOpt = getRealAlgothmByName(algorithmName);

        Algorithm copy = algorithmOpt.map(Algorithm::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean existAlgorithm(String algorithmName) {
        return getRealAlgothmByName(algorithmName).isPresent();
    }

    public static void addNewAlgorithm(Algorithm newAlgorithm){
        //TODO
    }

    public static Algorithm removeAlgorithm(String algorithmName){
        return null;
        //TODO
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
     * warning: return real object of real-time data
     */
    private static Optional<Algorithm> getRealAlgothmByName(String algorithmName) {
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
