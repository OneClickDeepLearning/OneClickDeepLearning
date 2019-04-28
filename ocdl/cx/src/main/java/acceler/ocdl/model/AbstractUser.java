package acceler.ocdl.model;

import java.io.Serializable;

public abstract class AbstractUser implements Serializable {
    private static final long serialVersionUID = -2767605614048989439L;

    private InnerUser.Role role;

}
