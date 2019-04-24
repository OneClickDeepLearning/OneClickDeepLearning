package com.ocdl.client;

import com.ocdl.client.service.ConsumerService;
import com.ocdl.client.util.CmdHelper;
import com.ocdl.client.util.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

@Component
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private final String MODELPATH = getClass().getResource("/models").getPath();


    @Autowired
    private ConsumerService consumer;

    public Client() { }

    public void run() {

        logger.info("Client is running....");

        // run consumer in a separated thread
        Client client = this;

        (new Thread(new Runnable() {
            @Override
            public void run() {
                consumer.createConsumer();
                consumer.run(client);
            }
        })).start();
    }

    public void downloadModel(String msg) {
        String[] modelInfo = msg.split(" ");
        String modelName = modelInfo[0].trim();
        String url = modelInfo[1].trim();

        logger.info("waiting for download the latest model:");

        try {
            FileTool.downLoadFromUrl(url, modelName, MODELPATH);
        } catch (IOException e) {
            logger.error("download failure" + e.getMessage());
        }
    }

}
