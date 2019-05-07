package acceler.ocdl.model;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class StorageLoader {
    public static final Logger logger = LoggerFactory.getLogger(StorageLoader.class);

    @Bean(name = "storageInit")
    public StorageLoader init() {
        return new StorageLoader();
    }

    public StorageLoader() {
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

        return data;
    }
}
