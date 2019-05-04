package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.utils.SerializationUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static acceler.ocdl.utils.TimeUtil.currentTime;


public class NewModel extends Model {
    private static final List<NewModel> newModelStorage = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Date commitTime;


    public NewModel() {
        super();
        this.status = Status.NEW;
    }

    public ApprovedModel convertToApprovedModel(long cachedVersion, long releaseVersion) {
        ApprovedModel model = new ApprovedModel();

        model.setName(this.name);
        model.status = Status.APPROVED;
        model.setApprovedTime(currentTime());
        model.setCachedVersion(cachedVersion);
        model.setReleasedVersion(releaseVersion);

        return model;
    }

    public RejectedModel convertToRejectedModel() {
        RejectedModel model = new RejectedModel();

        model.setName(this.name);
        model.status = Status.REJECTED;
        model.setRejectedTime(currentTime());

        return model;
    }

    public NewModel deepClone() {
        NewModel copy = new NewModel();
        copy.setName(this.name);
        copy.status = Status.NEW;
        copy.setCommitTime(this.commitTime);
        return copy;
    }


    /**
     * all static methods return real-time status of data, automatically synchronized with persistence.
     * to protect real-time data object, only return deep copy of the object.
     */

    public static Optional<NewModel> getNewModelByName(String name) {
        Optional<NewModel> modelOpt = getRealModelByName(name);

        NewModel copy = modelOpt.map(NewModel::deepClone).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean existNewModel(NewModel model) {
        return getRealModelByName(model.name).isPresent();
    }

    public static void addToStorage(NewModel model) {
        if (existNewModel(model)) {
            throw new ExistedException();
        }

        newModelStorage.add(model);
        persistence();
    }

    public static Optional<NewModel> removeFromStorage(String name) {
        Optional<NewModel> modelOpt = getRealModelByName(name);

        modelOpt.ifPresent(newModelStorage::remove);
        persistence();

        NewModel copy = modelOpt.map(NewModel::deepClone).orElse(null);

        return Optional.ofNullable(copy);
    }

    private static void persistence() {
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTANCE.NEW_MODELS);
        SerializationUtils.dump(newModelStorage, dumpFile);
        lock.writeLock().lock();
    }

    /**
     * warning: methods below return real object of real-time data, be careful to be expose reference.
     */
    private static Optional<NewModel> getRealModelByName(String name) {
        lock.readLock().lock();
        Optional<NewModel> modelOpt = newModelStorage.stream().filter(m -> (m.name.equals(name))).findFirst();
        lock.readLock().unlock();

        return modelOpt;
    }

    public Date getCommitTime() {
        return this.commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }

    public static NewModel[] getAllNewModels(){
        lock.readLock().lock();
        NewModel[] newModels = (NewModel[]) newModelStorage.stream().map(NewModel::deepClone).toArray();
        lock.readLock().unlock();

        return newModels;
    }
}
