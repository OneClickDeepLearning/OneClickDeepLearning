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


public class RejectedModel extends Model {
    private static final Logger logger = LoggerFactory.getLogger(RejectedModel.class);

    private static List<RejectedModel> rejectedModelStorage;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();


    private static List<RejectedModel> getRejectedModelStorage() {
        if (rejectedModelStorage == null) {
            logger.error("RejectedModelStorage instance is null");
            throw new InitStorageException("RejectedModelStorage instance is null");
        }

        return rejectedModelStorage;
    }

    static void initializeStorage() {
        if (rejectedModelStorage == null) {
            logger.info("[init] RejectedModelStorage instance initialization executed");
            File rejectedModelFile = new File(CONSTANTS.PERSISTENCE.REJECTED_MODELS);
            try {
                rejectedModelStorage = (ArrayList) StorageLoader.loadStorage(rejectedModelFile);
            } catch (NotFoundException nfe) {
                rejectedModelStorage = new ArrayList<>();
            }
        }

        logger.warn("Storage initialization only allow been executed at init time");
    }

    private static void persistence() {
        File dumpFile = new File(CONSTANTS.PERSISTENCE.REJECTED_MODELS);
        SerializationUtils.dump(rejectedModelStorage, dumpFile);
    }

    private static Optional<RejectedModel> getRealModelByName(String name) {
        lock.readLock().lock();
        Optional<RejectedModel> modelOpt = getRejectedModelStorage().stream().filter(m -> (m.name.equals(name))).findFirst();
        lock.readLock().unlock();

        return modelOpt;
    }

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
            throw new ExistedException("rejected model already existed");
        }

        lock.writeLock().lock();
        getRejectedModelStorage().add(model.deepCopy());
        persistence();
        lock.writeLock().unlock();
    }

    public static Optional<RejectedModel> removeFromStorage(String name) {
        Optional<RejectedModel> modelOpt = getRealModelByName(name);

        lock.writeLock().lock();
        modelOpt.ifPresent(getRejectedModelStorage()::remove);
        persistence();
        lock.writeLock().unlock();

        RejectedModel copy = modelOpt.map(RejectedModel::deepCopy).orElse(null);
        return Optional.ofNullable(copy);
    }

    public static RejectedModel[] getAllRejectedModels() {
        lock.readLock().lock();
        RejectedModel[] rejectedModels = getRejectedModelStorage().stream().map(RejectedModel::deepCopy).toArray(size -> new RejectedModel[size]);
        lock.readLock().unlock();

        return rejectedModels;
    }


    private Date rejectedTime;

    public RejectedModel() {
        super();
        this.status = Status.REJECTED;
    }

    public RejectedModel deepCopy() {
        RejectedModel copy = new RejectedModel();
        copy.setName(this.name);
        copy.status = Status.REJECTED;
        copy.setRejectedTime(this.rejectedTime);

        return copy;
    }

    public NewModel convertToNewModel() {
        NewModel newModel = new NewModel();
        newModel.setName(this.name);
        newModel.status = Status.NEW;
        newModel.setCommitTime(currentTime());

        return newModel;
    }

    public Date getRejectedTime() {
        return this.rejectedTime;
    }

    public void setRejectedTime(Date rejectedTime) {
        this.rejectedTime = rejectedTime;
    }
}