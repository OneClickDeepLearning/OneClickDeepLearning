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
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "template")
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "description")
    private String description;

    @Column(name = "ref_id")
    @JsonProperty("ref_id")
    private String refId;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"description", "algorithm_list", "suffix_list", "user_list", "created_at", "deleted_at", "is_deleted"})
    private Project project;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonProperty("template_category")
    @JsonIgnoreProperties(value = {"template_list", "parent", "children"})
    private TemplateCategory templateCategory;

}
