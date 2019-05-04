package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class DBUserService implements UserService {

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        InnerUser innerUser = this.userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
        return innerUser != null;
    }

    public InnerUser getUserByCredentials(AuthController.UserCredentials loginUser) {
        return userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
    }

    @Override
    public InnerUser getUserBySourceID(InnerUser.OauthSource source, String ID) throws NotFoundException {
        return null;
    }

    @Override
    public InnerUser createUser(String ID, InnerUser.OauthSource source) throws ExistedException {
        return null;
    }
}
