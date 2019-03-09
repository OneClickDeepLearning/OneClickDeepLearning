package acceler.ocdl.persistence.crud;

import acceler.ocdl.model.User;

import java.util.Optional;

public interface UserCrud {

    User getUserById(Long userId);

    User getUserByAccountAndPassword(String account, String password);
}
