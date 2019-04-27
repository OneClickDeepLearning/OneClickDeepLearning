package acceler.ocdl;

import acceler.ocdl.model.Algorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class OcdlApplication {

    private static final Logger logger = LoggerFactory.getLogger(OcdlApplication.class);
    public static void main(String[] args){

        SpringApplication.run(OcdlApplication.class, args);
        System.out.println("--Application Started--");
        logger.debug("--Application Started--");
    }


    @PreDestroy
    public void preDestory() {


        logger.debug("--PreDestory start--");
        logger.debug("--Exit--");
    }
}
