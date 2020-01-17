package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.OauthUser;

public interface UserService {

    boolean credentialCheck(AuthController.UserCredentials loginUser);

    User getUserByUsername(String userName) throws NotFoundException;

    User getUserBySourceID(OauthUser.OauthSource source, String ID) throws NotFoundException;

    User getUserByUserId(Long id) throws NotFoundException;

    //OauthUser createUser(OauthUser.OauthSource source, String ID) throws ExistedException;

    //InnerUser createUser(String userName, String password, AbstractUser.Role role) throws ExistedException;

    User saveUser(User user);
}
