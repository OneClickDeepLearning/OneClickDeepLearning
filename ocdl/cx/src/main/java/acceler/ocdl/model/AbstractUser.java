package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.exception.InitStorageException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public abstract class AbstractUser extends Storable implements Serializable {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractUser.class);

    private static List<AbstractUser> userListStorage = new ArrayList<>();
    protected static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected static AtomicLong userIdGenerator = new AtomicLong(1000);

    protected static List<AbstractUser> getUserListStorage() {
        if (userListStorage == null) {
            logger.error("userListStorage instance is null");
            throw new InitStorageException("UserListStorage instance is null");
        }
        return userListStorage;
    }

    private static void persistence() {
        File dumpFile = new File(CONSTANTS.PERSISTENCE.USERS);
        SerializationUtils.dump(userListStorage, dumpFile);
    }

    protected static Long getUniqueUserId() {
        return userIdGenerator.incrementAndGet();
    }

    protected static void insertUserToStorage(AbstractUser newUser) {
        lock.writeLock().lock();
        getUserListStorage().add(newUser.deepCopy());
        persistence();
        lock.writeLock().unlock();
    }

    static void initializeStorage(ArrayList<AbstractUser> users) {
        if (userListStorage != null) {
            userListStorage = users;
            logger.info("SserListStorage instance initialization executed");
        }

        //init admin user
        InnerUser adminUser= new InnerUser();
        adminUser.setUserName("admin");
        adminUser.setPassword("admin");
        adminUser.setRole(Role.MANAGER);
        adminUser.setUserId(1000L);
        userListStorage.add(adminUser);
        

        logger.warn("Storage initialization only allow been executed at init time");
    }


    protected Long userId;
    protected Role role;

    public abstract AbstractUser deepCopy();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        TEST, DEVELOPER, MANAGER
    }
}
