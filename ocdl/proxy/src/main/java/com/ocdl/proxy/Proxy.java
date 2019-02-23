package com.ocdl.proxy;

import org.springframework.stereotype.Component;
import com.ocdl.proxy.domain.Topic;
import com.ocdl.proxy.util.FileTool;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

@Component
public class Proxy {

    HashMap<String, Set<String>> preModel;
    HashMap<String, Set<String>> curModel;

    @Resource
    StorageService storage;

    @Resource
    MessageTransferService msgTransfer;

    public static String SOURCE = "/var/lib/jenkins/workspace/OneClickDeepLearning/models";
    public static String BUCKETNAME = "ocdl-model";

    public Proxy() {
        // create the preModel and curModel
        preModel = new HashMap<String, Set<String>>();
        curModel = new HashMap<String, Set<String>>();
    }

    public void run() {

        Proxy proxy = this;

        (new Thread(new Runnable() {
            @Override
            public void run() {
                msgTransfer.createConsumer();
                msgTransfer.consum(Topic.jkmsg, proxy);
            }
        })).start();

    }

    public void processMsg(String msg) {

        System.out.println("=================================================================");

        System.out.println("PreModel is: ");
        printModelMap(preModel);

        curModel = FileTool.listModel(SOURCE);
        System.out.println("CurModel is: ");
        printModelMap(curModel);

        HashMap<String, Set<String>> newModel = FileTool.getNewModels(curModel, preModel);

        for (String directory : newModel.keySet()) {

            newModel.get(directory).stream().forEach( v -> {

                File model = new File(Paths.get(SOURCE, directory, v).toString());

                // upload file to S3
                storage.createStorage();
                String modelName = directory+ "_" + v;
                storage.uploadObject(BUCKETNAME, modelName, model);

                // send message in kafka
                msgTransfer.createProducer();
                String content = modelName + " " + storage.getObkectUrl(BUCKETNAME, modelName);
                msgTransfer.send(Topic.mdmsg, content);
                System.out.println("produce message send...   ");

            });
        }


        preModel.clear();
        curModel.keySet().stream().forEach(key -> {
            preModel.put(key, curModel.get(key));
        });
        curModel.clear();

        System.out.println("=================================================================");
    }


    private void printModelMap( HashMap<String, Set<String>> models) {

        models.keySet().stream().forEach(key -> {
            System.out.println(key + ": ");
            System.out.print(models.get(key));
            System.out.println();
        });
    }




}
