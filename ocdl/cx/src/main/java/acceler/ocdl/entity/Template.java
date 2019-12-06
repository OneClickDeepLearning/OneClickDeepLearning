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
@Table(name = "template")
public class Template extends BaseEntity {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
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

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private TemplateCategory templateCategory;

}
