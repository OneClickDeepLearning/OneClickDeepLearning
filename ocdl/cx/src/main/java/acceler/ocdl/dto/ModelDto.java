package acceler.ocdl.dto;

import acceler.ocdl.model.Model;

import java.io.Serializable;

public class ModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String modelId;
    private String modelName;
    private String modelType;
    private String status;
    private String version;
    private String timeStamp;

    public String getModelId() { return modelId; }

    public void setModelId(String modelId) { this.modelId = modelId; }

    public String getModelName() { return modelName; }

    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getModelType() { return modelType; }

    public void setModelType(String modelType) { this.modelType = modelType; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getVersion() { return version; }

    public void setVersion(String version) { this.version = version; }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
}
