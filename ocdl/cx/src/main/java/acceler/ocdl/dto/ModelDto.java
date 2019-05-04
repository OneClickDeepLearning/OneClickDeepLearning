package acceler.ocdl.dto;

import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.ApprovedModel;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.NewModel;
import acceler.ocdl.model.RejectedModel;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ModelDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String modelName;
    private String algorithm;
    private String status;

    /**
     * releasedVersion + cachedVersion;
     */
    private String version;

    /**
     * <ol>
     *  <li>new model: model create time </li>
     *  <li>approval model: time that a model approved/li>
     *  <li>reject model: time that a model rejected</li>
     * </ol>
     */
    private String timeStamp;


    public String getModelName() { return modelName; }

    public void setModelName(String modelName) { this.modelName = modelName; }

    public String getAlgorithm() { return algorithm; }

    public void setAlgorithm(String algorithm) { this.algorithm = algorithm; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getVersion() { return version; }

    public void setVersion(String version) { this.version = version; }

    public String getTimeStamp() { return timeStamp; }

    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }

    public Model convertToModel(){

        Model model;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        try {
            if (status.equals(Model.Status.NEW.toString())) {
                model = new NewModel();
                model.setName(modelName);
                ((NewModel) model).setCommitTime(dateFormat.parse(timeStamp));
            } else if (status.equals(Model.Status.REJECTED.toString())) {
                model = new RejectedModel();
                model.setName(modelName);
                ((RejectedModel) model).setRejectedTime(dateFormat.parse(timeStamp));
            } else if (status.equals(Model.Status.APPROVED.toString())) {
                model = new ApprovedModel();
                model.setName(modelName);
                ((ApprovedModel) model).setApprovedTime(dateFormat.parse(timeStamp));
            } else {
                throw new OcdlException("Invalid status type in ModelDto.");
            }
        }catch (ParseException e) {
            throw new OcdlException("Invalid time format of ModelDto timestamp.");
        }

        return model;
    }
}
