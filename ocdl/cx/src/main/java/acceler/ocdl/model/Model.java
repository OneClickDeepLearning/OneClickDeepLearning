package acceler.ocdl.model;

import javax.persistence.*;

@Entity
@Table(name = "model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long modelId;

    @Column(name = "name")
    private String modelName;

    @Column(name = "model_type_id")
    private String modelType;

    @Column(name = "project_id")
    private String project;

    @Column(name = "url")
    private String url;

    @Enumerated()
    @Column(name = "status_id")
    private Status status;

    @Column(name = "big_version")
    private String version;

    @Column(name = "small_version")
    private String smallVersion;

    public Model(){}

    public Model(String modelName, String modelType, String project, String url) {
        this.modelName = modelName;
        this.modelType = modelType;
        this.project = project;
        this.url = url;
        this.status = Status.NEW;
        this.version = null;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public enum Status {
        NEW, APPROVAL, REJECT;

        public static Status getStatus(String status) {

            switch (status.toLowerCase()) {
                case "new":
                    return Status.NEW;
                case "approval":
                    return Status.APPROVAL;
                case "reject":
                    return Status.REJECT;
            }
            return null;
        }

        public static Status getStatus(int status) {

            switch (status) {
                case -1:
                    return Status.NEW;
                case 1:
                    return Status.APPROVAL;
                case 0:
                    return Status.REJECT;
            }
            return null;
        }
    }
}
