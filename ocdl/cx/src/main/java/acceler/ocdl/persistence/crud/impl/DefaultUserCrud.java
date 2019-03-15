package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.UserCrud;
import acceler.ocdl.persistence.dao.UserDao;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DefaultUserCrud implements UserCrud {

    @Resource
    private UserDao userDao;

    @Override
    public User getUserById(Long userId) {
        Optional<User> userOpt = this.userDao.findById(userId);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            return null;
        }
    }

    @Override
    public User getUserByAccountAndPassword(String account, String password) {
        List<User> userOpt = this.userDao.findByAccountAndPassword(account, password);
        if (userOpt.size()>0) {
            return userOpt.get(0);
        } else {
            return null;
        }
    }
}
