package acceler.ocdl.model;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class StorageLoader {
    public static final Logger logger = LoggerFactory.getLogger(StorageLoader.class);

    public StorageLoader() {
        logger.info("[init] storage files loading...");
        AbstractUser.initializeStorage();
        Algorithm.initializeStorage();
        NewModel.initializeStorage();
        RejectedModel.initializeStorage();
        Project.initializeStorage();
    }

    public static Object loadStorage(File serializedFile) throws NotFoundException {
        Object data = null;

        if (!serializedFile.exists() || !serializedFile.isFile()) {
            logger.warn("serializedFile[" + serializedFile + "] not found");
            throw new NotFoundException("serializedFile not found", "serializedFile not found");
        }
        data = SerializationUtils.load(serializedFile);
        logger.warn("serializedFile[" + serializedFile + "] load successfully");
        return data;
    }
}
