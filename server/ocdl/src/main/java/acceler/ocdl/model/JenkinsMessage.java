package acceler.ocdl.model;

import java.io.Serializable;

public class JenkinsMessage implements Serializable {

    //{"modelId":1,"modelName":"abc","version":"1.0"}
    private int modelId;
    private String modelName;
    private String version;

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JenkinsMessage msg = (JenkinsMessage) o;

        return modelId == msg.modelId? true : false;
    }

}
