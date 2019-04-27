package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.User;

public interface UserService {

     boolean credentialCheck(AuthController.UserCredentials loginUser);
     User getUserBySourceID(User.OauthSource source,String ID) throws NotFoundException;
     User createUser(String ID,User.OauthSource source) throws ExistedException;
}
