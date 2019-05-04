package acceler.ocdl.model;

import java.io.Serializable;

public class InnerUser extends AbstractUser implements Serializable {
    private String userName;
    private String password;

    public InnerUser() {
    }

    public InnerUser deepCopy() {
        InnerUser copy = new InnerUser();
        copy.userId = this.userId;
        copy.userName = this.userName;
        copy.password = this.password;
        return copy;
    }

    private InnerUser[] getRealInnerUser() {
        return (InnerUser[]) userListStorage.stream().filter(u -> u instanceof InnerUser).toArray();
    }
}
