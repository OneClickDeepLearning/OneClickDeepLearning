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

    public static Optional<OauthSource> getUserByOauthInfo(String userName, OauthSource oauthSource) {

        Optional<AbstractUser> userOpt = getRealUserByOauthInfo(userName, oauthSource);

        AbstractUser copy = userOpt.map(OauthUser::deepCopy).orElse(null);

        return Optional.ofNullable(copy);
    }

    private static Optional<AbstractUser> getRealUserByOauthInfo(String authServerUserId, OauthSource oauthSource) {
        return userListStorage.stream()
                .filter(u -> u instanceof OauthUser)
                .filter(u -> ((OauthUser) u).authServerUserId.equals(authServerUserId) && ((OauthUser) u).oauthSource == oauthSource)
                .findFirst();
    }

    public enum OauthSource {
        GITHUB, GOOGLE
    }
}
