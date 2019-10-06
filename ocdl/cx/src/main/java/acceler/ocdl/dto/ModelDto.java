package acceler.ocdl.dto;

import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.ApprovedModel;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.NewModel;
import acceler.ocdl.model.RejectedModel;
import acceler.ocdl.utils.TimeUtil;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

public class ModelDto implements Serializable,Comparable<ModelDto> {
    private static final long serialVersionUID = 1L;

    private String modelId;
    private String modelName;
    private String algorithm;
    private String status;
    private String modelFileName;
    private String ownerId;
    private String ownerName;
    private String comments;

    /**
     * releasedVersion + cachedVersion;
     */
    private String version;

    /**
     * <ol>
     * <li>new model: model create time </li>
     * <li>approval model: time that a model approved/li>
     * <li>reject model: time that a model rejected</li>
     * </ol>
     */
    private String timeStamp;


    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getModelFileName() { return modelFileName; }

    public void setModelFileName(String modelFileName) { this.modelFileName = modelFileName; }

    public String getOwnerId() { return ownerId; }

    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }

    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public String getComments() { return comments; }

    public void setComments(String comments) { this.comments = comments; }

    public Model convertToModel() {

        Model model;

        try {
            //optional value
            Date time = this.timeStamp != null ? TimeUtil.convertStringToDate(this.timeStamp) : null;

            if (this.status.toUpperCase().equals(Model.Status.NEW.toString())) {
                model = new NewModel();
                model.setModelId(Long.parseLong(this.modelId));
                model.setName(this.modelName);
                model.setOwnerId(Long.parseLong(this.ownerId));
                model.setComments(this.comments);
                ((NewModel) model).setCommitTime(time);
            } else if (this.status.toUpperCase().equals(Model.Status.REJECTED.toString())) {
                model = new RejectedModel();
                model.setModelId(Long.parseLong(this.modelId));
                model.setName(this.modelName);
                model.setOwnerId(Long.parseLong(this.ownerId));
                model.setComments(this.comments);
                ((RejectedModel) model).setRejectedTime(time);
            } else if (this.status.toUpperCase().equals(Model.Status.APPROVED.toString())) {
                model = new ApprovedModel();
                model.setModelId(Long.parseLong(this.modelId));
                model.setName(this.modelName);
                model.setOwnerId(Long.parseLong(this.ownerId));
                model.setComments(this.comments);
                ((ApprovedModel) model).setApprovedTime(time);
            } else {
                throw new OcdlException("Invalid status type in ModelDto.");
            }
        } catch (ParseException e) {
            throw new OcdlException("Invalid time format of ModelDto timestamp.");
        }

        return model;
    }

    @Override
    public int compareTo(ModelDto modelDto) {
        Date thisTime, modleDtoTime;
        try {
            thisTime = TimeUtil.convertStringToDate(this.timeStamp);
            modleDtoTime = TimeUtil.convertStringToDate(modelDto.timeStamp);
        } catch (ParseException e) {
            throw new OcdlException("Invalid time format of timestamp.");
        }

        return thisTime.compareTo(modleDtoTime)*(-1);
    }
}
