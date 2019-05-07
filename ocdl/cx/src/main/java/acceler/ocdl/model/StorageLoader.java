package acceler.ocdl.model;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageLoader {

/*    @Bean(name = "storageLoader")*/
    public StorageLoader init(){
        return new StorageLoader();
    }

    public StorageLoader(){

    }
}
