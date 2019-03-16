package acceler.ocdl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PreDestroy;


@SpringBootApplication
public class OcdlApplication {
    public static void main(String[] args){
        SpringApplication.run(OcdlApplication.class, args);
    }


    @PreDestroy
    public void clean() {
        System.out.println("exit.....");
    }
}
