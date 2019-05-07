package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.InitStorageException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static acceler.ocdl.utils.TimeUtil.currentTime;


public class NewModel extends Model {
    private static final Logger logger = LoggerFactory.getLogger(NewModel.class);

    private static List<NewModel> newModelStorage = new ArrayList<>();
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    private static List<NewModel> getNewModelStorage() {
        if (newModelStorage == null) {
            logger.error("NewModelStorage instance is null");
            throw new InitStorageException("NewModelStorage instance is null");
        }

        return newModelStorage;
    }

    static void initializeStorage() {
        if (newModelStorage == null) {
            logger.info("[init] NewModelStorage instance initialization executed");
            File newModelsFile = new File(CONSTANTS.PERSISTENCE.NEW_MODEL);
            try {
                newModelStorage = (ArrayList) StorageLoader.loadStorage(newModelsFile);
            } catch (NotFoundException nfe) {
                newModelStorage = new ArrayList<>();
            }
        }

        logger.warn("Storage initialization only allow been executed at init time");
    }

    public static Optional<NewModel> getNewModelByName(String name) {
        Optional<NewModel> modelOpt = getRealModelByName(name);

        NewModel copy = modelOpt.map(NewModel::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    public static boolean existNewModel(NewModel model) {
        return getRealModelByName(model.name).isPresent();
    }

    public static void addToStorage(NewModel model) {
        if (existNewModel(model)) {
            throw new ExistedException();
        }

        lock.writeLock().lock();
        getNewModelStorage().add(model.deepCopy());
        persistence();
        lock.writeLock().unlock();
    }

    public static Optional<NewModel> removeFromStorage(String name) {
        Optional<NewModel> modelOpt = getRealModelByName(name);

        lock.writeLock().lock();
        modelOpt.ifPresent(getNewModelStorage()::remove);
        persistence();
        lock.writeLock().unlock();

        NewModel copy = modelOpt.map(NewModel::deepCopy).orElse(null);
        return Optional.ofNullable(copy);
    }

    private static void persistence() {
        File dumpFile = new File(CONSTANTS.PERSISTENCE.NEW_MODEL);
        SerializationUtils.dump(getNewModelStorage(), dumpFile);
    }

    private static Optional<NewModel> getRealModelByName(String name) {
        lock.readLock().lock();
        Optional<NewModel> modelOpt = getNewModelStorage().stream().filter(m -> (m.name.equals(name))).findFirst();
        lock.readLock().unlock();

        return modelOpt;
    }

    public static NewModel[] getAllNewModels() {
        lock.readLock().lock();
        NewModel[] newModels = getNewModelStorage().stream().map(NewModel::deepCopy).toArray(size -> new NewModel[size]);
        lock.readLock().unlock();

        return newModels;
    }


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

    public NewModel deepCopy() {
        NewModel copy = new NewModel();
        copy.setName(this.name);
        copy.status = Status.NEW;
        copy.setCommitTime(this.commitTime);

        return copy;
    }

    public Date getCommitTime() {
        return this.commitTime;
    }

    public void setCommitTime(Date commitTime) {
        this.commitTime = commitTime;
    }
}
