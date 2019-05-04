package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.utils.SerializationUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class InnerUser extends AbstractUser implements Serializable {
    private static List<InnerUser> innerUserListStorage = new ArrayList<>();
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Long userId;

    private OauthSource oauthSource;

    private String authServerUserId;

    public InnerUser() {
    }

    public InnerUser deepCopy(){
        InnerUser copy = new InnerUser();
        copy.userId = this.userId;
        copy.authServerUserId = this.authServerUserId;
        copy.oauthSource = this.oauthSource;

        return copy;
    }

    public Optional<InnerUser> getUserByOauthInfo(String userName, OauthSource oauthSource){
        Optional<InnerUser> userOpt = getRealUserByOauthInfo(userName, oauthSource);

        InnerUser copy = userOpt.map(InnerUser::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }



    private Optional<InnerUser> getRealUserByOauthInfo(String userName, OauthSource oauthSource) {
        lock.readLock().lock();
        Optional<InnerUser> userOpt = innerUserListStorage.stream().filter(user -> (user.authServerUserId.equals(userName) && user.oauthSource == oauthSource)).findFirst();
        lock.readLock().unlock();
        return userOpt;
    }

    public Long getUserId() {
        return this.userId;
    }

    public String getAuthServerUserId() {
        return this.authServerUserId;
    }

    public OauthSource getOauthSource() {
        return this.oauthSource;
    }

    public Role getRole() {
        return this.role;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setAuthServerUserId(String authServerUserId) {
        this.authServerUserId = authServerUserId;
    }

    public void setOauthSource(OauthSource oauthSource) {
        this.oauthSource = oauthSource;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public enum Role {
        TEST, DEVELOPER, MANAGER;

        public static Role getRole(String role) {

            switch (role.toLowerCase()) {
                case "manager":
                    return Role.MANAGER;
                case "developer":
                    return Role.DEVELOPER;
                case "test":
                    return Role.TEST;
            }
            return null;
        }
    }

    public enum OauthSource {
        GITHUB, GOOGLE
    }

    private static void persistence(){
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTANCE.USERS);
        SerializationUtils.dump(innerUserListStorage, dumpFile);
        lock.writeLock().unlock();
    }
}
