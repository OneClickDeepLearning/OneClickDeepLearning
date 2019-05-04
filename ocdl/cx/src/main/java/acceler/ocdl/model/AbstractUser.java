package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.utils.SerializationUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractUser implements Serializable {
    protected static final long serialVersionUID = -2767605614048989439L;

    protected static List<AbstractUser> userListStorage = new ArrayList<>();
    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    protected Long userId;
    protected Role role;

    public abstract AbstractUser deepCopy();

    private static void persistence() {
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTANCE.USERS);
        SerializationUtils.dump(userListStorage, dumpFile);
        lock.writeLock().unlock();
    }

    public enum Role {
        TEST, DEVELOPER, MANAGER
    }
}
