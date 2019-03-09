package acceler.ocdl.model;

import acceler.ocdl.dto.ModelDto;

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
    private Status statusId;

    @Column(name = "big_version")
    private Long bigVersion;

    @Column(name = "small_version")
    private Long smallVersion;

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

    public Status getStatusId() {
        return statusId;
    }

    public void setStatusId(Status statusId) {
        this.statusId = statusId;
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

    public static enum Status {
        NEW, APPROVAL, REJECT
    }

}
