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

        logger.debug("Client is running....");
        System.out.println("Client is running....");

        // run consumer in a separated thread
        Client client = this;

        (new Thread(new Runnable() {
            @Override
            public void run() {
                consumer.createConsumer();
                consumer.run(client);
            }
        })).start();

        // ask for a text
        String pwd = CmdHelper.runCommand("pwd");
        System.out.println("the pwd is:");
        System.out.println(pwd);

        System.out.println(MODELPATH);
        String path = Paths.get(MODELPATH, "Sentence2label").toString();
        File file = new File(path);
        if (file.exists()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please input the text: ");
            String request = scanner.nextLine();

            StringBuilder cmd = new StringBuilder();
            cmd.append("python3 ");
            cmd.append(Paths.get(MODELPATH,"Sentence2label/sentence2vec.py"));
            cmd.append(" -s ");
            cmd.append(request);
            cmd.append(" -w2v_path ");
            cmd.append(Paths.get(MODELPATH, "Sentence2label/models/CBOW.model").toString());
            cmd.append(" -stopword ");
            cmd.append(Paths.get(MODELPATH, "Sentence2label/data/baidu+chuanda.txt").toString());
            cmd.append(" -model_path ");
            cmd.append(Paths.get(MODELPATH, "Sentence2label/models/80000NN.h5py"));
            cmd.append(" -label_file ");
            cmd.append(Paths.get(MODELPATH, "Sentence2label/data/all_labels.txt"));

            String result = CmdHelper.runCommand(cmd.toString());
            System.out.println("The result is: ");
            System.out.println(result.isEmpty());
            System.out.println(result);
        }

    }

    public void downloadModel(String msg) {
        String[] modelInfo = msg.split(" ");
        String modelName = modelInfo[0].trim();
        String url = modelInfo[1].trim();

        System.out.println("waiting for download the latest model:");

        try {
            FileTool.downLoadFromUrl(url, modelName, MODELPATH );

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("download failure");
        }
    }

}
