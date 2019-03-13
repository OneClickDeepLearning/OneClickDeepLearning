package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.UserCrud;
import acceler.ocdl.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DBUserService implements UserService {
    @Autowired
    private UserCrud userCrud;

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        User user = this.userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
        return user != null;
    }


    public User getUserByCredentials(AuthController.UserCredentials loginUser) {
        return userCrud.getUserByAccountAndPassword(loginUser.account, loginUser.password);
    }
}
