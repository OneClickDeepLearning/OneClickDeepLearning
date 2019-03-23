package acceler.ocdl.model;

import javax.persistence.*;

public class User {

    private long userId;

    private String userName;

    private String account;

    private String password;

    private Role role;

    public User() {
    }

    public User(String userName, String account, String password, Role role) {
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.role = role;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public enum Role {
        TEST, DEVELOPER,MANAGER;

        public static Role getRole(String role) {

            switch (role.toLowerCase()) {
                case "manager":
                    return Role.MANAGER;
                case "developer":
                    return Role.DEVELOPER;
                case "test":
                    return Role.TEST;
            }
            return null;
        }
    }
}
