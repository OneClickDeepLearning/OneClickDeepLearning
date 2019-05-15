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
        logger.info("waiting for download the latest model:");
        String[] modelInfo = msg.split("\\s+");
        if (modelInfo.length == 0) {
            return;
        }
        String modelName = modelInfo[0].trim();
        String url = modelInfo[1].trim();

        try {
            FileTool.downLoadFromUrl(url, modelName, MODELBASEPATH);
            currentModelName = modelName;
        } catch (IOException e) {
            logger.info("download failure" + e.getMessage());
        }
    }

}
