package acceler.ocdl.model;

import acceler.ocdl.dto.ModelTypeDto;

import java.io.Serializable;

public class ModelType implements Serializable {


//    private long modelTypeId;

    private String modelTypeName;

    private int currentBigVersion = -1;

    private int currentSmallVersion = -1;

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
