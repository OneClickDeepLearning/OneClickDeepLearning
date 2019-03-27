package acceler.ocdl.persistence;

import acceler.ocdl.model.User;

import java.util.List;
import java.util.Optional;

public interface UserCrud {

    User getUserById(long userId);

    User getUserByAccountAndPassword(String account, String password);
}
