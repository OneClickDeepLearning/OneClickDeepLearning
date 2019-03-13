package acceler.ocdl.exception;

public class KuberneteException extends RuntimeException {

    public KuberneteException(String message) {
        super(String.format("kubernete exception: %s", message));
    }
}
