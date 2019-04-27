package acceler.ocdl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class User implements Serializable {
    private static final long serialVersionUID = -2767605614048989439L;
    private static List<User> userListStorage = new ArrayList<>();
    private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private Long userId;

    private OauthSource oauthSource;

    private String authServerUserId;

    private Role role;

    public User() {
    }

    public User deepCopy(){
        User copy = new User();
        copy.userId = this.userId;
        copy.authServerUserId = this.authServerUserId;
        copy.oauthSource = this.oauthSource;

        return copy;
    }

    public Optional<User> getUserByOauthInfo(String userName, OauthSource oauthSource){
        Optional<User> userOpt = getRealUserByOauthInfo(userName, oauthSource);

        User copy = userOpt.map(User::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }



    private Optional<User> getRealUserByOauthInfo(String userName, OauthSource oauthSource) {
        lock.readLock().lock();
        Optional<User> userOpt = userListStorage.stream().filter(user -> (user.authServerUserId.equals(userName) && user.oauthSource == oauthSource)).findFirst();
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
}
