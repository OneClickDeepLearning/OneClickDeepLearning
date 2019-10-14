package acceler.ocdl.model;

import acceler.ocdl.utils.TimeUtil;

import java.util.Date;

public class ApprovedModel extends Model implements Cloneable {
    private Long releasedVersion;
    private Long cachedVersion;
    private Date approvedTime;


    public ApprovedModel() {
        super();
        this.status = Status.APPROVED;
    }

    public ApprovedModel deepCopy() {
        ApprovedModel copy = new ApprovedModel();

        copy.setModelId(this.modelId);
        copy.setSuffix(this.suffix);
        copy.setName(this.name);
        copy.setStatus(this.status);
        copy.setOwnerId(this.ownerId);
        copy.setComments(this.comments);
        copy.setReleasedVersion(this.releasedVersion);
        copy.setCachedVersion(this.cachedVersion);
        copy.setApprovedTime(this.approvedTime);

        return copy;
    }

    public NewModel convertToNewModel(String comments) {
        NewModel newModel = new NewModel();
        newModel.setModelId(this.modelId);
        newModel.setSuffix(this.suffix);
        newModel.setName(this.name);
        newModel.setOwnerId(this.ownerId);
        newModel.setComments(comments);
        newModel.setCommitTime(TimeUtil.currentTime());

        return newModel;
    }

    public Long getReleasedVersion() {
        return this.releasedVersion;
    }

    public Long getCachedVersion() {
        return this.cachedVersion;
    }

    public Date getApprovedTime() {
        return this.approvedTime;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }

    public void setReleasedVersion(Long releasedVersion) {
        this.releasedVersion = releasedVersion;
    }

    public void setCachedVersion(Long cachedVersion) {
        this.cachedVersion = cachedVersion;
    }


}
