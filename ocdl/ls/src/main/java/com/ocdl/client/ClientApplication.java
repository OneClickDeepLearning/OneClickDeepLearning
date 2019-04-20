package com.ocdl.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ClientApplication implements CommandLineRunner {


    @Autowired
    Client client;


    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }

    public void run(String... args) throws Exception {

        client.run();

    }
}
