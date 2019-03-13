package acceler.ocdl.exception;

public class OcdlException extends RuntimeException {
    private String frontEndErrorMsg;

    public OcdlException(String frontEndErrorMsg) {
        super(frontEndErrorMsg);
        this.frontEndErrorMsg = frontEndErrorMsg;
    }

    public String getFrontEndErrorMsg() {
        return frontEndErrorMsg;
    }
}
