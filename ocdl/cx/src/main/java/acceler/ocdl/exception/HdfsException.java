package acceler.ocdl.exception;

public class HdfsException extends OcdlException {

    public HdfsException(String message) {
        super(String.format("HDFS exception: %s", message));
    }
}
