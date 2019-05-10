package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dto.ModelDto;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import static acceler.ocdl.utils.TimeUtil.convertDateToString;

public abstract class Model extends Storable implements Serializable {
    private static final AtomicLong modelIdGenerator = new AtomicLong(100000);

    protected Long modelId;
    protected String name;
    protected Status status;

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public Long getModelId() {
        return modelId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public static Long generateModelId() {
        return modelIdGenerator.incrementAndGet();
    }

    public ModelDto convertToModelDto(Model model) {
        ModelDto modelDto = new ModelDto();
        modelDto.setModelId(model.getModelId().toString());
        modelDto.setModelName(model.getName());
        modelDto.setStatus(model.getStatus().toString());

        if (model instanceof NewModel) {
            NewModel newModel = (NewModel) model;
            modelDto.setTimeStamp(convertDateToString(newModel.getCommitTime()));
        } else if (model instanceof RejectedModel) {
            RejectedModel rejectedModel = (RejectedModel) model;
            modelDto.setTimeStamp(convertDateToString(rejectedModel.getRejectedTime()));
        } else {
            ApprovedModel approvedModel = (ApprovedModel) model;
            modelDto.setTimeStamp(convertDateToString(approvedModel.getApprovedTime()));
            modelDto.setAlgorithm(Algorithm.getAlgorithmOfApprovedModel(approvedModel).getAlgorithmName());

            String version = CONSTANTS.NAME_FORMAT.MODELDTO_VERSION.replace("{release_version}", Long.toString(approvedModel.getReleasedVersion()).replace("{cached_version}", Long.toString(approvedModel.getCachedVersion())));
            modelDto.setVersion(version);
        }

        return modelDto;

    }

    public enum Status {
        NEW, APPROVED, REJECTED
    }
}
