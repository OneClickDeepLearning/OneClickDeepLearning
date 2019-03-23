package acceler.ocdl.dto;

import acceler.ocdl.model.Model;

import java.io.Serializable;

public class ModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private long modelId;
    private String modelName;
    private String modelType;
    private String project;
    private String url;
    private String status;
    private String version;

    public long getModelId() { return modelId; }

    public void setModelId(long modelId) { this.modelId = modelId; }

    public String getModelName() { return modelName; }

    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getModelType() { return modelType; }

    public void setModelType(String modelType) { this.modelType = modelType; }

    public String getProject() { return project; }

    public void setProject(String project) { this.project = project; }

    public String getUrl() { return url; }

    public void setUrl(String url) { this.url = url; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getVersion() { return version; }

    public void setVersion(String version) { this.version = version; }

}
