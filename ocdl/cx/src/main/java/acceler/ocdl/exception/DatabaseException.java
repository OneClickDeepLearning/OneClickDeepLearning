package acceler.ocdl.exception;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(String.format("Database exception: %s", message));
    }
}
