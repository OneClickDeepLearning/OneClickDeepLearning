package acceler.ocdl.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class ApprovedModel extends Model implements Cloneable {

    @Getter
    @Setter
    private long releasedVersion;

    @Getter
    @Setter
    private long cachedVersion;

    @Getter
    @Setter
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
}
