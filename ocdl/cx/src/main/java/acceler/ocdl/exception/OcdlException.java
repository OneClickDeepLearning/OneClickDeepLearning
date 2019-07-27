package acceler.ocdl.exception;

public class OcdlException extends RuntimeException {

    private String promptErrorMessage;

    public OcdlException(String promptErrorMessage) {
        super(promptErrorMessage);
        this.promptErrorMessage = promptErrorMessage;
    }

    public String getMessage() {
        return promptErrorMessage;
    }
}
