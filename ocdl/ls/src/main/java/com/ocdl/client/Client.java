package com.ocdl.client;

import com.ocdl.client.service.ConsumerService;
import com.ocdl.client.util.FileTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class Client {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    @Value("${models.path}")
    private String MODELBASEPATH;

    @Autowired
    private ConsumerService consumer;

    public static String currentModelName = "unet_membrane.tflite";

    public Client() { }

    public void run() {

        System.out.println("Client is running....");
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
        String[] modelInfo = msg.split("\\s+");
        if (modelInfo.length == 0) {
            return;
        }
        String modelName = modelInfo[0].trim();
        String url = modelInfo[1].trim();

        System.out.println("waiting for download the latest model:");
        logger.info("waiting for download the latest model:");

        try {
            FileTool.downLoadFromUrl(url, modelName, MODELBASEPATH);
            currentModelName = modelName;
        } catch (IOException e) {
            System.out.println("download failure" + e.getMessage());
            logger.info("download failure" + e.getMessage());
        }
    }

}
