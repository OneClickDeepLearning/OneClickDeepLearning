package acceler.ocdl;

import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class EnvInitializer {

    public EnvInitializer() {
        checkSerializedFileDir();
    }

    private void checkSerializedFileDir() {
        File serializedFileDir = new File(CONSTANTS.PERSISTENCE._BASE);
        if (!serializedFileDir.exists()) {
            serializedFileDir.mkdirs();
        }
    }

}
