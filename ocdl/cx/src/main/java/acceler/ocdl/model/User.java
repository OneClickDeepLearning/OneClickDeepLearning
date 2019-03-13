package acceler.ocdl.model;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "name")
    private String userName;

    @Column(name = "account")
    private String account;

    @Column(name = "password")
    private String password;

    @Column(name = "project_id")
    private Long projectId;


    private int type;
    private Role role;

    public User() {
    }

    public User(String userName, String account, String password, int type, Role role, Long projectId) {
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
        this.projectId = projectId;
    }


    public User(Long userId, String userName, String account, String password, int type, Role role, Long projectId) {
        this.userId = userId;
        this.userName = userName;
        this.account = account;
        this.password = password;
        this.type = type;
        this.role = role;
        this.projectId = projectId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    public Long getProjectId() { return projectId; }

    public void setProjectId(Long projectId) { this.projectId = projectId; }


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
        MANAGER, DEVELOPER, TEST;

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
