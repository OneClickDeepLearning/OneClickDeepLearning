package acceler.ocdl.entity;

import org.codehaus.jackson.annotate.JsonProperty;
import javax.persistence.Column;
import java.io.Serializable;

public abstract class BaseEntity implements Serializable {

    protected static final long serialVersionUID = 1L;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private String createdAt;

    @Column(name = "deleted_at")
    @JsonProperty("deleted_at")
    private String deletedAt;

    @Column(name = "is_deleted")
    @JsonProperty("is_deleted")
    private boolean isdeleted;

}
