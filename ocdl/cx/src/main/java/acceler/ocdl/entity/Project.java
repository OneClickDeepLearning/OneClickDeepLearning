package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "project")
public class Project extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    @Column(name = "ref_id")
    @JsonProperty("ref_id")
    private String refId;

//    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
//    @JsonProperty("model_list")
//    @JsonIgnoreProperties(value = "project")
//    @JsonIgnore
//    private List<Model> modelList;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @JsonProperty("algorithm_list")
    @JsonIgnoreProperties(value = {"project", "model_list"})
    private Set<Algorithm> algorithmList;

    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @JsonProperty("suffix_list")
    @JsonIgnoreProperties(value = "project")
    private Set<Suffix> suffixList;

//    @OneToMany(mappedBy = "project")
//    @JsonProperty("project_data_list")
//    @JsonIgnoreProperties(value = "project")
//    @JsonIgnore
//    private List<ProjectData> projectDataList;

//    @OneToMany(mappedBy = "project")
//    @JsonProperty("template_list")
//    @JsonIgnoreProperties(value = "project")
//    @JsonIgnore
//    private List<Template> templateList;
//
//    @OneToMany(mappedBy = "project")
//    @JsonProperty("template_category_list")
//    @JsonIgnoreProperties(value = "project")
//    @JsonIgnore
//    private List<TemplateCategory> templateCategoryList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "r_user_role",
            joinColumns = {@JoinColumn(name = "project_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @JsonProperty("user_list")
    @JsonIgnoreProperties(value = "projectList")
    private Set<User> userList;

}
