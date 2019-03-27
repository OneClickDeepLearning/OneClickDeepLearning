package acceler.ocdl.dto;

import acceler.ocdl.model.Model;

import java.io.Serializable;

public class IncomeModelDto implements Serializable{

    private static final long serialVersionUID = 1L;

    private String modelName;
    private String modelType;
    private String preStatus;
    private String destStatus;
    private int bigVersion = -1;

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

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getDestStatus() {
        return destStatus;
    }

    public void setDestStatus(String destStatus) {
        this.destStatus = destStatus;
    }

    public int getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(int bigVersion) {
        this.bigVersion = bigVersion;
    }
}

