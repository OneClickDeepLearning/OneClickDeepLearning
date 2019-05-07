package acceler.ocdl.model;

import java.io.Serializable;
import java.util.Optional;

public class InnerUser extends AbstractUser implements Serializable {
    private String userName;
    private String password;


    public InnerUser() {
    }

    private static InnerUser[] getRealInnerUser() {
        return getUserListStorage().stream().filter(u -> u instanceof InnerUser).toArray(size -> new InnerUser[size]);
    }

    public static Optional<InnerUser> getUserByUserName(String userName) {
        final InnerUser[] realInnerUsers = getRealInnerUser();

        InnerUser target = null;

        for (InnerUser user : realInnerUsers) {
            if (user.userName.equals(userName)) {
                target = user.deepCopy();
                break;
            }
        }

        return Optional.ofNullable(target);
    }

    public static boolean existUser(String userName) {
        return getUserListStorage().stream()
                .filter(abstractUser -> abstractUser instanceof InnerUser)
                .anyMatch(user -> ((InnerUser) user).getUserName().equals(userName));
    }

    public static InnerUser createNewUser(String userName, String password) {
        InnerUser newUser = new InnerUser();
        newUser.setUserId(AbstractUser.getUniqueUserId());
        newUser.setRole(Role.DEVELOPER);
        newUser.setUserName(userName);
        newUser.setPassword(password);
        AbstractUser.insertUserToStorage(newUser);

        return newUser;
    }


    @Override
    public InnerUser deepCopy() {
        InnerUser copy = new InnerUser();
        copy.userId = this.userId;
        copy.role = this.role;
        copy.userName = this.userName;
        copy.password = this.password;

        return copy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
