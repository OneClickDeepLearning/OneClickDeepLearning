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


public class RejectedModel extends Model {

    private static final List<RejectedModel> rejectedModelStorage = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    private Date rejectedTime;

    public RejectedModel() {
        super();
        this.status = Status.REJECTED;
    }

    public NewModel convertToNewModel() {
        NewModel newModel = new NewModel();
        newModel.setName(this.name);
        newModel.status = Status.NEW;
        newModel.setCommitTime(currentTime());
        return newModel;
    }

    public RejectedModel deepCopy() {
        RejectedModel copy = new RejectedModel();
        copy.setName(this.name);
        copy.status = Status.REJECTED;
        copy.setRejectedTime(this.rejectedTime);

        return copy;
    }


    /**
     * all static methods return real-time status of data, automatically synchronized with persistence.
     * to protect real-time data object, only return deep copy of the object.
     */

    public static Optional<RejectedModel> getRejectedModelByName(String name) {
        Optional<RejectedModel> modelOpt = getRealModelByName(name);

        RejectedModel copy = modelOpt.map(RejectedModel::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean existRejectedModel(RejectedModel model) {
        return getRealModelByName(model.name).isPresent();
    }

    public static void addToStorage(RejectedModel model) {
        if (existRejectedModel(model)) {
            throw new ExistedException();
        }

        rejectedModelStorage.add(model);
        persistence();
    }

    public static Optional<RejectedModel> removeFromStorage(String name) {
        Optional<RejectedModel> modelOpt = getRealModelByName(name);

        modelOpt.ifPresent(rejectedModelStorage::remove);
        persistence();

        RejectedModel copy = modelOpt.map(RejectedModel::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }


    /**
     * warning: methods below return real object of real-time data, be careful to be expose reference.
     */
    private static Optional<RejectedModel> getRealModelByName(String name) {
        lock.readLock().lock();
        Optional<RejectedModel> modelOpt = rejectedModelStorage.stream().filter(m -> (m.name.equals(name))).findFirst();
        lock.readLock().unlock();

        return modelOpt;
    }

    public Date getRejectedTime() {
        return this.rejectedTime;
    }

    public void setRejectedTime(Date rejectedTime) {
        this.rejectedTime = rejectedTime;
    }

    private static void persistence(){
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTANCE.REJECTED_MODELS);
        SerializationUtils.dump(rejectedModelStorage, dumpFile);
        lock.writeLock().unlock();
    }
}