package acceler.ocdl.utils;

import acceler.ocdl.model.InnerUser;

import java.util.Date;

public class SecurityUser {

    private Date requestTime;
    private InnerUser innerUser;

    public SecurityUser(Date requestTime, InnerUser innerUser){
        this.requestTime = requestTime;
        this.innerUser = innerUser;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public InnerUser getInnerUser() {
        return innerUser;
    }

}
