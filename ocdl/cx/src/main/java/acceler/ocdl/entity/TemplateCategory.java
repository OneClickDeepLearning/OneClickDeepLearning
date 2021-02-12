package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "template_category")
public class TemplateCategory extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "shared")
    private Boolean shared;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"parent", "children", "template_list", "project", "created_at", "deleted_at", "is_deleted"})
    private TemplateCategory parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"parent", "project", "created_at", "deleted_at", "is_deleted"})
    @Where(clause = "is_deleted = false")
    private List<TemplateCategory> children;

    @Where(clause = "is_deleted=false")
    @OneToMany(mappedBy = "templateCategory", fetch = FetchType.EAGER)
    @JsonProperty("template_list")
    @JsonIgnoreProperties(value = {"template_category", "project", "template_category", "created_at", "deleted_at", "is_deleted"})
    private List<Template> templateList;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"description", "algorithm_list", "suffix_list", "user_list", "created_at", "deleted_at", "is_deleted"})
    private Project project;
}
