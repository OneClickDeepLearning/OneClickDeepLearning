package acceler.ocdl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "r_user_role")
public class RUserRole extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"user_roles","model_list", "operate_model_list", "user_data_list","updated_at", "created_at", "is_deleted", "deleted_at"})
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"user_list", "model_list", "created_at", "deleted_at"})
    private Project project;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = {"users", "created_at", "is_deleted", "deleted_at"})
    private Role role;
}
