package acceler.ocdl.persistence;

import acceler.ocdl.model.User;

public interface UserCrud {

    User getUserByAccountAndPassword(String account, String password);
}
