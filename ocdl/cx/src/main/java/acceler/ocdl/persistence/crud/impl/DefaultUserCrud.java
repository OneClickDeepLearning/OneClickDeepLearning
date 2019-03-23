package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.persistence.Persistence;
import acceler.ocdl.persistence.crud.UserCrud;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultUserCrud implements UserCrud {

    private Persistence persistence;

    public User getUserById(long userId) {

        List<User> users = persistence.getUser();

        Optional<User> user = users.stream().filter(u -> u.getUserId() == userId)
                .findAny();

        if (user.isPresent()) return user.get();
        else return null;
    }


    public User getUserByAccountAndPassword(String account, String password, List<User> users) {

        List<User> users = persistence.getUser();

        Optional<User> user = users.stream().filter(u -> u.getAccount().equals(account) && u.getPassword().equals(password))
                .findAny();

        if (user.isPresent()) return user.get();
        else return null;
    }
}
