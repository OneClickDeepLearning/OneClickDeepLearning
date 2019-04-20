package acceler.ocdl.model;

import java.io.Serializable;


public abstract class Model implements Serializable {
    protected static final long serialVersionUID = -2767605614048989439L;

    protected String name;

    protected Status status;

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }


    public enum Status {
        NEW, APPROVED, REJECTED
    }
}
