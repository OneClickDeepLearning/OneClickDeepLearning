package acceler.ocdl.model;

import java.io.Serializable;
import java.util.Optional;

public class OauthUser extends AbstractUser implements Serializable {
    private OauthSource oauthSource;
    private String authServerUserId;

    @Override
    public OauthUser deepCopy() {
        OauthUser copy = new OauthUser();
        copy.userId = this.userId;
        copy.oauthSource = this.oauthSource;
        copy.authServerUserId = this.authServerUserId;

        return copy;
    }

    public static OauthUser[] getOauthUsers() {
        return (OauthUser[]) userListStorage.stream().filter(user -> (user instanceof OauthUser)).toArray();
    }

    public static boolean existUser(OauthUser.OauthSource source, String sourceUserId){
        return userListStorage.stream()
                .filter(abstractUser -> abstractUser instanceof OauthUser)
                .filter(u -> ((OauthUser) u).getOauthSource() == source)
                .anyMatch(u -> ((OauthUser) u).getAuthServerUserId().equals(sourceUserId));
    }

    public static Optional<OauthUser> getUserBySourceID(OauthUser.OauthSource source, String sourceId) {
        OauthUser[] oauthUsers = getOauthUsers();
        OauthUser target = null;
        for (OauthUser user : oauthUsers) {
            if (user.getOauthSource() == source && user.getOauthSource().equals(sourceId)) {
                target = user.deepCopy();
                break;
            }
        }
        return Optional.ofNullable(target);
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
