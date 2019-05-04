package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.OauthUser;

public interface UserService {

    boolean credentialCheck(AuthController.UserCredentials loginUser);

    OauthUser getUserBySourceID(OauthUser.OauthSource source, String ID) throws NotFoundException;

    OauthUser createUser(OauthUser.OauthSource source, String ID) throws ExistedException;
}
