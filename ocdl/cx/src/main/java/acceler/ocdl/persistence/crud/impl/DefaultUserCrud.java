package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.UserCrud;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultUserCrud implements UserCrud {

    @Override
    public User getUserById(Long userId) {
        return null;
    }

    @Override
    public User getUserByAccountAndPassword(String account, String password) {
        return null;
    }
}
