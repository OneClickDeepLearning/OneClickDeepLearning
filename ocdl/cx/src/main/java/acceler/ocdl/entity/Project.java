package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;
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

    @Where(clause = "is_deleted=false")
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @JsonProperty("algorithm_list")
    @JsonIgnoreProperties(value = {"project", "model_list"}, allowSetters = true)
    private Set<Algorithm> algorithmList;

    @Where(clause = "is_deleted=false")
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @JsonProperty("suffix_list")
    @JsonIgnoreProperties(value = "project", allowSetters = true)
    private Set<Suffix> suffixList;

    @Where(clause = "is_deleted=false")
    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER)
    @JsonProperty("user_roles")
    @JsonIgnoreProperties(value = "project", allowSetters = true)
    private Set<RUserRole> userRoles;

}
