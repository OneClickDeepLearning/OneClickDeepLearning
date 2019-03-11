package acceler.ocdl.dto;

import acceler.ocdl.model.Model;

import java.io.Serializable;

public class IncomeModelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long modelId;
    private String modelName;
    private Long modelTypeId;
    private Long projectId;
    private String url;
    private String status;
    private int bigVersion;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(int bigVersion) {
        this.bigVersion = bigVersion;
    }

    public Model convert2Model() {
        Model model = new Model();

        model.setId(this.modelId);
        model.setName(this.modelName);
        model.setModelTypeId(this.modelTypeId);
        model.setProjectId(this.projectId);
        model.setUrl(this.url);
        model.setStatus(Model.Status.valueOf(status));

        return model;

    }
}
