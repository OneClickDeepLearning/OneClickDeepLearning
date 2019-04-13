package acceler.ocdl.model;

import java.util.Date;

public class ApprovedModel extends Model implements Cloneable {

    private long releasedVersion;

    private long cachedVersion;

    private Date approvedTime;


    public ApprovedModel() {
        super();
        this.status = Status.APPROVED;
    }


    public ApprovedModel deepCopy() {
        ApprovedModel copy = new ApprovedModel();
        copy.setName(this.name);
        copy.status = Status.APPROVED;
        copy.setReleasedVersion(this.releasedVersion);
        copy.setCachedVersion(this.cachedVersion);
        copy.setApprovedTime(this.approvedTime);

        return copy;
    }

    public long getReleasedVersion() {
        return this.releasedVersion;
    }

    public long getCachedVersion() {
        return this.cachedVersion;
    }

    public Date getApprovedTime() {
        return this.approvedTime;
    }

    public void setReleasedVersion(long releasedVersion) {
        this.releasedVersion = releasedVersion;
    }

    public void setCachedVersion(long cachedVersion) {
        this.cachedVersion = cachedVersion;
    }

    public void setApprovedTime(Date approvedTime) {
        this.approvedTime = approvedTime;
    }
}
