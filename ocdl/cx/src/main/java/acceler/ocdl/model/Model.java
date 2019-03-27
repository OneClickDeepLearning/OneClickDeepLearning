package acceler.ocdl.model;


public class Model {

    private String name;

    private String url;

    private Status status;

    private long bigVersion;

    private long smallVersion;

    private ModelType modelType;


    public Model(){}

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
