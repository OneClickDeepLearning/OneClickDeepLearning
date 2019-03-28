package acceler.ocdl.persistence.impl;

import acceler.ocdl.model.User;

import acceler.ocdl.persistence.UserCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class DefaultUserCrud implements UserCrud {

    @Autowired
    private Persistence persistence;

    public User getUserById(long userId) {

        List<User> users = persistence.getUserList();

        Optional<User> user = users.stream().filter(u -> u.getUserId() == userId)
                .findAny();

        if (user.isPresent()) return user.get();
        else return null;
    }


    public User getUserByAccountAndPassword(String account, String password) {

        List<User> users = persistence.getUserList();

        Optional<User> user = users.stream().filter(u -> u.getAccount().equals(account) && u.getPassword().equals(password))
                .findAny();

        if (user.isPresent()) return user.get();
        else return null;
    }
}
