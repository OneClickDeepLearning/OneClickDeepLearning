package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.model.User;

public interface UserService {

    public boolean credentialCheck(AuthController.UserCredentials loginUser);

}
