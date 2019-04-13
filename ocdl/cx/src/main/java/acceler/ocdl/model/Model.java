package acceler.ocdl.model;

import lombok.*;

import java.io.Serializable;


public abstract class Model implements Serializable {
    protected static final long serialVersionUID = -2767605614048989439L;

    @Getter
    @Setter
    protected String name;

    @Getter
    protected Status status;


    public enum Status {
        NEW, APPROVED, REJECTED
    }
}
