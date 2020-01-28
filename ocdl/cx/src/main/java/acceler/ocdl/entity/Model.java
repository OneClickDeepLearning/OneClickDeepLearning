package acceler.ocdl.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.codehaus.jackson.annotate.JsonProperty;
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
    @JoinColumn(name = "owner")
    @JsonIgnoreProperties(value = {"modelList", "operateModelList"})
    private User owner;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_operator")
    @JsonProperty("last_operator")
    @JsonIgnoreProperties(value = {"modelList", "operateModelList"})
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

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "algorithm_id")
    @JsonIgnoreProperties(value = "modelList")
    private Algorithm algorithm;

    @Column(name = "suffix")
    private String suffix;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties(value = "modelList")
    private Project project;


}
