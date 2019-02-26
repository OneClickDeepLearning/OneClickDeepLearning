package acceler.ocdl.model;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String userName;
    private String account;
    private String password;
    private int type;
    private Role role;

    public User() {}

    public User(String userName, String account, String password, int type, Role role) {
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
    }


    public User(Long userId, String userName, String account, String password, int type, Role role) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

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

    public int getType(){ return type; }

    public void setType(int type){ this.type = type; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }


    public enum Role {
        MANAGER, DEVELOPER,TEST;

        public static Role getRole(String role) {

            switch(role.toLowerCase()) {
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
