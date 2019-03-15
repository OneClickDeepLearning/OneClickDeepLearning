package com.ocdl.proxy;

import com.ocdl.proxy.domain.Topic;
import com.ocdl.proxy.util.FileTool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.ocdl.proxy.service.StorageService;
import com.ocdl.proxy.service.MessageTransferService;

import javax.annotation.Resource;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Set;

@Component
public class Proxy implements ProxyCallBack{

    HashMap<String, HashMap<String, Set<String>>> preModels;
    HashMap<String, Set<String>> curModel;

    @Resource
    StorageService storage;

    @Resource
    MessageTransferService msgTransfer;

    public static String SOURCE;
    public static String BUCKETNAME;

    public Proxy() {
        // create the preModel and curModel
        preModels = new HashMap<>();
        curModel = new HashMap<>();
    }

    @Value("${jenkins.server.workspacePath}")
    public static void setSOURCE(String SOURCE) { Proxy.SOURCE = SOURCE; }

    @Value("${S3.server.bucketName}")
    public static void setBUCKETNAME(String BUCKETNAME) { Proxy.BUCKETNAME = BUCKETNAME; }

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

    @Override
    public void processMsg(String msg) {

        System.out.println("=================================================================");

        String[] projectInfo = msg.split(" ");
        Path path = Paths.get(SOURCE, projectInfo[0]);

        HashMap<String, Set<String>> preModel = null;
        System.out.println("PreModel is: ");
        if (preModels.containsKey(projectInfo[0].trim())) {
            preModel = preModels.get(projectInfo[0].trim());
        } else {
            preModel = new HashMap<String, Set<String>>();
            preModels.put(projectInfo[0].trim(), preModel);
        }
        printModelMap(preModel);

        curModel = FileTool.listModel(path.toString());
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
                msgTransfer.send(projectInfo[1], content);
                System.out.println("produce message send...   ");

            });
        }


        preModel.clear();
        for (String key : curModel.keySet()) {
            preModel.put(key, curModel.get(key));
        }
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
