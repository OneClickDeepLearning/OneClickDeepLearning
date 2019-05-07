package acceler.ocdl.model;

import java.io.Serializable;
import java.util.Optional;


public class OauthUser extends AbstractUser implements Serializable {
    private OauthSource oauthSource;
    private String authServerUserId;


    private static OauthUser[] getOauthUsers() {
        return getUserListStorage().stream().filter(user -> (user instanceof OauthUser)).toArray(size -> new OauthUser[size]);
    }

    public static boolean existUser(OauthUser.OauthSource source, String sourceUserId) {
        return getUserListStorage().stream()
                .filter(abstractUser -> abstractUser instanceof OauthUser)
                .filter(u -> ((OauthUser) u).getOauthSource() == source)
                .anyMatch(u -> ((OauthUser) u).getAuthServerUserId().equals(sourceUserId));
    }

    public static Optional<OauthUser> getUserBySourceID(OauthUser.OauthSource source, String sourceServerUserId) {
        OauthUser[] oauthUsers = getOauthUsers();

        OauthUser target = null;

        for (OauthUser user : oauthUsers) {
            if (user.getOauthSource() == source && user.getAuthServerUserId().equals(sourceServerUserId)) {
                target = user.deepCopy();
                break;
            }
        }

        return Optional.ofNullable(target);
    }

    public static OauthUser createNewUser(OauthUser.OauthSource source, String sourceServerUserId) {
        OauthUser newUser = new OauthUser();

        newUser.setUserId(AbstractUser.getUniqueUserId());
        newUser.setRole(Role.DEVELOPER);
        newUser.setOauthSource(source);
        newUser.setAuthServerUserId(sourceServerUserId);
        AbstractUser.insertUserToStorage(newUser);

        return newUser;
    }


    @Override
    public OauthUser deepCopy() {
        OauthUser copy = new OauthUser();
        copy.userId = this.userId;
        copy.oauthSource = this.oauthSource;
        copy.authServerUserId = this.authServerUserId;
        return copy;
    }

    public OauthSource getOauthSource() {
        return oauthSource;
    }

    public void setOauthSource(OauthSource oauthSource) {
        this.oauthSource = oauthSource;
    }

    public String getAuthServerUserId() {
        return authServerUserId;
    }

    public void setAuthServerUserId(String authServerUserId) {
        this.authServerUserId = authServerUserId;
    }

    public enum OauthSource {
        GITHUB, GOOGLE
    }
}
