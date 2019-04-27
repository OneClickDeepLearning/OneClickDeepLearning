package acceler.ocdl.exception;

public class KuberneteException extends OcdlException {

    public KuberneteException(String message) {
        super(String.format("kubernete exception: %s", message));
    }
}
