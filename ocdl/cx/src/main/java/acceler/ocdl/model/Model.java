package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.utils.TimeUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

import static acceler.ocdl.utils.TimeUtil.convertDateToString;

public abstract class Model extends Storable implements Serializable {
    private static final AtomicLong modelIdGenerator = new AtomicLong(getInitModelId());

    protected Long modelId;
    protected String name;
    protected Long ownerId;
    protected Status status;
    protected String suffix;
    protected String comments;

    public String getName() {
        return this.name;
    }

    public Status getStatus() {
        return this.status;
    }

    public Long getModelId() {
        return modelId;
    }

    public String getSuffix() { return suffix; }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setModelId(Long modelId) {
        this.modelId = modelId;
    }

    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getComments() { return comments; }

    public Long getOwnerId() { return ownerId; }

    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public void setComments(String comments) { this.comments = comments; }

    public static Long generateModelId() {
        return modelIdGenerator.incrementAndGet();
    }

    public static Long getInitModelId() {
        Date current = TimeUtil.currentTime();
        return current.getTime();
    }

    public String getModelFileName() {
        return CONSTANTS.NAME_FORMAT.STAGED_MODEL.replace("{modelId}", modelId.toString()).replace("{suffix}", suffix);
    }

    public ModelDto convertToModelDto(Model model) {
        ModelDto modelDto = new ModelDto();
        modelDto.setModelId(model.getModelId().toString());
        modelDto.setModelFileName(model.getModelFileName());
        modelDto.setModelName(model.getName());
        modelDto.setStatus(model.getStatus().toString());
        modelDto.setComments(model.getComments());

        System.out.println("when convet to model Dto");
        System.out.println(model.getName());
        System.out.println(model.getOwnerId());
        if (model.getOwnerId() != null) {
            modelDto.setOwnerId(model.getOwnerId().toString());
            InnerUser user = (InnerUser)AbstractUser.findUserById(model.getOwnerId());
            modelDto.setOwnerName(user.getUserName());
        }


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

            String version = CONSTANTS.NAME_FORMAT.MODELDTO_VERSION.replace("{release_version}", Long.toString(approvedModel.getReleasedVersion()));
            version = version.replace("{cached_version}", Long.toString(approvedModel.getCachedVersion()));
            modelDto.setVersion(version);
        }

        return modelDto;

    }

    public enum Status {
        NEW, APPROVED, REJECTED, RELEASED
    }
}
