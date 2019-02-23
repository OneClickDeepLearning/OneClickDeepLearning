package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.service.UserService;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Service
public class InMemoryUserService implements UserService {
    private static final List<User> validUsers = new LinkedList<>();

    public InMemoryUserService() {
        System.out.println("[INFO] loading in-memory user list");
        User user1 = new User();
        user1.setUserId(1001L);
        user1.setAccount("u1001");
        user1.setPassword("p1001");
        validUsers.add(user1);

        User user2 = new User();
        user2.setUserId(1002L);
        user2.setAccount("u1002");
        user2.setPassword("p1002");
        validUsers.add(user2);

        User user3 = new User();
        user3.setUserId(1003L);
        user3.setAccount("u1003");
        user3.setPassword("p1003");
        validUsers.add(user3);
    }

    @Override
    public boolean credentialCheck(final User loginUser) {
        Boolean isValidUser = false;

        for (User u : validUsers) {
            if (u.getUserId().equals(loginUser.getUserId()) &&
                    u.getAccount().equals(loginUser.getAccount()) &&
                    u.getPassword().equals(loginUser.getPassword())) {
                isValidUser = true;
                break;
            }
        }
        return isValidUser;
    }
}
