package acceler.ocdl.entity;


import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "type")
    private String type;

    @Column(name = "password")
    @ColumnTransformer(
            read = "CAST(AES_DECRYPT(UNHEX(password), '$*^@!#') as char(128))",
            write = "HEX(AES_ENCRYPT(?, '$*^@!#'))"
    )
    private String password;

    @ManyToMany
    @JoinTable(
            name = "r_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    @OneToMany(mappedBy = "user")
    private List<UserData> userDataList;
}
