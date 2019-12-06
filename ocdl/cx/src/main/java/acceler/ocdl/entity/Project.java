package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "project")
    private List<Model> modelList;

    @OneToMany(mappedBy = "project")
    private List<Algorithm> algorithmList;

    @OneToMany(mappedBy = "project")
    private List<Suffix> suffixList;

    @OneToMany(mappedBy = "project")
    @JsonProperty("project_data_list")
    private List<ProjectData> projectDataList;

    @OneToMany(mappedBy = "project")
    @JsonProperty("template_list")
    private List<Template> templateList;

    @OneToMany(mappedBy = "project")
    @JsonProperty("template_category_list")
    private List<TemplateCategory> templateCategoryList;

    @ManyToMany
    @JoinTable(
            name = "r_user_project",
            joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonProperty("user_list")
    @JsonIgnoreProperties(value = "projectList")
    private List<User> userList;


}
