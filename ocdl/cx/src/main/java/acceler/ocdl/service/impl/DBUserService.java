package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.UserCrud;
import acceler.ocdl.service.UserService;

public class DBUserService implements UserService {
    private UserCrud userCrud;

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        User user = this.userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
        return user != null;
    }

    @Override
    public User getUserByCredentials(AuthController.UserCredentials loginUser) {
        return userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
    }
}
