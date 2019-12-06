package acceler.ocdl.entity;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "algorithm")
public class Algorithm extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "current_cached_version")
    @JsonProperty("current_cached_version")
    private Integer currentCachedVersion;

    @Column(name = "current_released_version")
    @JsonProperty("current_released_version")
    private Integer currentReleasedVersion;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

}