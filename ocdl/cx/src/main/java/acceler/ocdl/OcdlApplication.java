package acceler.ocdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
