package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "model")
public class Model extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ModelStatus status;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"project_list", "user_data_list", "model_list", "operate_model_list", "project", "roles"})
    private User owner;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonProperty("last_operator")
    @JsonIgnoreProperties(value = {"project_list", "user_data_list", "model_list", "operate_model_list", "project", "roles"})
    private User lastOperator;

    @Column(name = "comments")
    private String comments;

    @Column(name = "ref_id")
    @JsonProperty("ref_id")
    private String refId;

    @Column(name = "cached_version")
    @JsonProperty("cached_version")
    private Integer cachedVersion;

    @Column(name = "released_version")
    @JsonProperty("released_version")
    private Integer releasedVersion;

    @Transient
    @JsonProperty("is_cached_version")
    private Boolean isCachedVersion;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"model_list", "project"})
    private Algorithm algorithm;

    @Column(name = "suffix")
    private String suffix;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = {"algorithm_list", "suffix_list", "user_list", "model_list"})
    private Project project;

    @Column(name = "is_released")
    @JsonProperty("is_released")
    private Boolean isReleased;

}
