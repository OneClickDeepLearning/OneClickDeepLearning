package acceler.ocdl.model;

import acceler.ocdl.dto.ModelTypeDto;

import javax.persistence.*;

@Entity
@Table(name = "model_type")
public class ModelType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long modelTypeId;

    @Column(name = "name")
    private String modelTypeName;

    @Column(name = "project_id")
    private Long projectId;

    public Long getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Long modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public String getName() {
        return modelTypeName;
    }

    public void setName(String name) {
        this.modelTypeName = name;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ModelTypeDto convert2ModelDto() {
        ModelTypeDto modelTypeDto = new ModelTypeDto();
        modelTypeDto.setId(this.modelTypeId);
        modelTypeDto.setName(this.modelTypeName);

        return modelTypeDto;
    }

}
