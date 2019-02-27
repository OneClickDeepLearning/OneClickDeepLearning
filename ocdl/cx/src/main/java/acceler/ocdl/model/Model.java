package acceler.ocdl.model;


public class Model {

    //column: id, name, model_type, project, url, status, version
    private Long modelId;
    private String modelName;
    private String modelType;
    private String project;
    private String url;
    private Status status;
    private String version;


    public Model() {}

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

            switch(status.toLowerCase()) {
                case "new":
                    return Status.NEW;
                case "approval":
                    return Status.APPROVAL;
                case "reject":
                    return Status.REJECT;
            }
            return null;
        }
    }
}
