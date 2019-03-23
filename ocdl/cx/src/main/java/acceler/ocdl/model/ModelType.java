package acceler.ocdl.model;

import acceler.ocdl.dto.ModelTypeDto;

import javax.persistence.*;

public class ModelType {

    private long modelTypeId;

    private String modelTypeName;

    private int currentBigVersion;

    private int currentSmallVersion;

    public long getModelTypeId() {
        return modelTypeId;
    }

    public void setModelTypeId(long modelTypeId) {
        this.modelTypeId = modelTypeId;
    }

    public String getModelTypeName() {
        return modelTypeName;
    }

    public void setModelTypeName(String modelTypeName) {
        this.modelTypeName = modelTypeName;
    }

    public int getCurrentBigVersion() {
        return currentBigVersion;
    }

    public void setCurrentBigVersion(int currentBigVersion) {
        this.currentBigVersion = currentBigVersion;
    }

    public int getCurrentSmallVersion() {
        return currentSmallVersion;
    }

    public void setCurrentSmallVersion(int currentSmallVersion) {
        this.currentSmallVersion = currentSmallVersion;
    }
}
