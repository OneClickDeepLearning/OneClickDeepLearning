package acceler.ocdl.model;

import javax.persistence.*;

@Entity
public class ModelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long modelTypeId;

    @Column(name = "name")
    private String name;

    @Column(name = "project_id")
    private Long projectId;

    public Long getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Long modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
