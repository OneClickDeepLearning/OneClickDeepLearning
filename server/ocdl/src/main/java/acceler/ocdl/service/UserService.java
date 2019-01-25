package acceler.ocdl.service;

import acceler.ocdl.model.User;

public interface UserService {

    public boolean credentialCheck(User loginUser);
}
