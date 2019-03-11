package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;

public interface UserService {
    public boolean credentialCheck(AuthController.UserCredentials loginUser);
}
