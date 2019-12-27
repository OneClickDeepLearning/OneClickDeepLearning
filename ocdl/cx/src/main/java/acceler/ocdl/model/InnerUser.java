package acceler.ocdl.model;

import java.io.Serializable;
import java.util.Optional;

public class InnerUser extends AbstractUser implements Serializable {
    private String userName;
    private String password;


    public InnerUser() {
    }

    private static InnerUser[] getRealInnerUser() {
        lock.readLock().lock();
        InnerUser[] innerUsers = getUserListStorage().stream().filter(u -> u instanceof InnerUser).toArray(size -> new InnerUser[size]);
        lock.readLock().unlock();

        return innerUsers;
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
        lock.readLock().lock();
        boolean exist = getUserListStorage().stream()
                .filter(abstractUser -> abstractUser instanceof InnerUser)
                .anyMatch(user -> ((InnerUser) user).getUserName().equals(userName));
        lock.readLock().unlock();

        return exist;
    }

    public static InnerUser createNewUser(String userName, String password, AbstractUser.Role role) {
        InnerUser newUser = new InnerUser();
        newUser.setUserId(AbstractUser.getUniqueUserId());
        newUser.setRole(role);
        newUser.setUserName(userName);
        newUser.setPassword(password);
        AbstractUser.insertUserToStorage(newUser);

        return newUser;
    }


    @Override
    public InnerUser deepCopy() {
        System.out.println(this.userName);
        System.out.println(this.password);

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
