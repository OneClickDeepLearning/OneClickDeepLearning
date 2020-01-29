package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
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
    private Boolean isDeleted;

}
