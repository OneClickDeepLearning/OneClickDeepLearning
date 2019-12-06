package acceler.ocdl.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "template_category")
public class TemplateCategory extends BaseEntity{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "shared")
    private Boolean shared;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private TemplateCategory parent;

    @OneToMany(mappedBy = "parent")
    private List<TemplateCategory> children;

    @OneToMany(mappedBy = "templateCategory")
    private List<Template> templateList;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
