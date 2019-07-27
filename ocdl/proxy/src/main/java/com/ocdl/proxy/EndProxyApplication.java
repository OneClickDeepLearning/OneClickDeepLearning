package com.ocdl.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EndProxyApplication implements CommandLineRunner {

    @Autowired
    Proxy proxy;

    public static void main(String[] args) {
        SpringApplication.run(EndProxyApplication.class, args);
    }

    //access command line arguments
    @Override
    public void run(String... args) throws Exception {

        //Proxy proxy = new Proxy();
        proxy.run();
    }

}
