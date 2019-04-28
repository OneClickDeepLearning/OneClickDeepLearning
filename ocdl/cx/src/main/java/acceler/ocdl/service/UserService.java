package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.InnerUser;

public interface UserService {
     boolean credentialCheck(AuthController.UserCredentials loginUser);
     InnerUser getUserBySourceID(InnerUser.OauthSource source, String ID) throws NotFoundException;
     InnerUser createUser(String ID, InnerUser.OauthSource source) throws ExistedException;
}
