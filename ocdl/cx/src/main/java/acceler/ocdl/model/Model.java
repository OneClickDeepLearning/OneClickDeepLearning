package acceler.ocdl.model;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;

import javax.annotation.Resource;
import javax.persistence.*;

@Entity
@Table(name = "model")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "model_type_id")
    private Long modelTypeId;

    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "url")
    private String url;

    @Enumerated()
    @Column(name = "status_id")
    private Status status;

    @Column(name = "big_version")
    private Long bigVersion;

    @Column(name = "small_version")
    private Long smallVersion;

    @Transient
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "model_type_id", referencedColumnName = "id")
    private ModelType modelType;

    @Transient
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    public Model(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(Long modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Long getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(Long bigVersion) {
        this.bigVersion = bigVersion;
    }

    public Long getSmallVersion() {
        return smallVersion;
    }

    public void setSmallVersion(Long smallVersion) {
        this.smallVersion = smallVersion;
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void setModelType(ModelType modelType) {
        this.modelType = modelType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public static enum Status {
        NEW, APPROVAL, REJECT
    }

    public ModelDto convert2ModelDto() {
        ModelDto modelDto = new ModelDto();

        System.out.println(this.modelType);

        modelDto.setModelId(this.id);
        modelDto.setModelName(this.name);
        modelDto.setModelType(this.modelType.getName());
        modelDto.setProject(this.project.getProjectName());
        modelDto.setUrl(this.url);
        modelDto.setStatus(this.status.toString());
        modelDto.setVersion(bigVersion + "." + smallVersion);

        return modelDto;
    }



}
