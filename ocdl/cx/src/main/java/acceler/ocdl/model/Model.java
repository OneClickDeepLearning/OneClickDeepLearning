package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dto.ModelDto;
import com.amazonaws.services.opsworks.model.App;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


public abstract class Model implements Serializable {
    protected static final long serialVersionUID = -2767605614048989439L;

    protected String name;

    protected Status status;

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModelDto convertToModelDto(Model model) {

        ModelDto modelDto = new ModelDto();
        modelDto.setModelName(model.getName());
        modelDto.setStatus(model.getStatus().toString());

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        if (model instanceof NewModel) {
            NewModel newModel = (NewModel)model;
            modelDto.setTimeStamp(dateFormat.format(newModel.getCommitTime()));
        } else if (model instanceof RejectedModel) {
            RejectedModel rejectedModel = (RejectedModel)model;
            modelDto.setTimeStamp(dateFormat.format(rejectedModel.getRejectedTime()));
        } else {
            ApprovedModel approvedModel = (ApprovedModel)model;
            modelDto.setTimeStamp(dateFormat.format(approvedModel.getApprovedTime()));
            modelDto.setAlgorithm(Algorithm.getAlgorithmOfApprovedModel(approvedModel).getAlgorithmName());

            String version = CONSTANTS.NAME_FORMAT.MODELDTO_VERSION.replace("{release_version}",Long.toString(approvedModel.getReleasedVersion()).replace("{cached_version}", Long.toString(approvedModel.getCachedVersion())));
            modelDto.setVersion(version);
        }

        return modelDto;

    }

    public enum Status {
        NEW, APPROVED, REJECTED
    }
}
