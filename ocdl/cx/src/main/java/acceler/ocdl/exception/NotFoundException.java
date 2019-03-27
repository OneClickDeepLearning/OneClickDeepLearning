package acceler.ocdl.exception;

public class NotFoundException extends RuntimeException {

    private String logMessage;
    private String responseMessage;

    public NotFoundException(String logMessage, String responseMessage){
        super(responseMessage);
        this.logMessage = logMessage;
        this.responseMessage = responseMessage;
    }
}
