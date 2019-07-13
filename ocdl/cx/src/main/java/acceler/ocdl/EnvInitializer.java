package acceler.ocdl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class EnvInitializer {

    @Value("${data.path}")
    public static String defaultDataPath;

    public EnvInitializer() {
    }

    private void checkSerializedFileDir() {
        File serializedFileDir = new File(defaultDataPath);
        if (!serializedFileDir.exists()) {
            serializedFileDir.mkdirs();
        }
    }

}
