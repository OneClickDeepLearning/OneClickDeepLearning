package acceler.ocdl.model;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long userId;

    @Column(name = "name")
    private String userName;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "project_id")
    private long projectId;

    @Transient
    private int type;
    @Column(name = "role")
    private Role role;

    public User() {
    }

    public User(String userName, String account, String password, int type, Role role, long projectId) {
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
        this.projectId = projectId;
    }


    public User(long userId, String userName, String account, String password, int type, Role role, long projectId) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
        this.projectId = projectId;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public long getProjectId() { return projectId; }

    public void setProjectId(long projectId) { this.projectId = projectId; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userId==user.userId? true : false;

    }

//    @Override
//    public int hashCode() {
//        return userId.hashCode();
//    }


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
