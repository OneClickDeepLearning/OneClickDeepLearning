package acceler.ocdl.model;

import javax.persistence.*;

public class Model {

    private long id;

    private String name;

    private long modelTypeId;

    private String url;

    private Status status;

    private long bigVersion;

    private long smallVersion;

    private ModelType modelType;


    public Model(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) { this.url = url; }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getBigVersion() {
        return bigVersion;
    }

    public void setBigVersion(long bigVersion) { this.bigVersion = bigVersion; }

    public long getSmallVersion() {
        return smallVersion;
    }

    public void setSmallVersion(long smallVersion) {
        this.smallVersion = smallVersion;
    }

    public ModelType getModelType() { return modelType; }

    public void setModelType(ModelType modelType) { this.modelType = modelType; }


    public static enum Status {
        NEW, APPROVAL, REJECT
    }

}
