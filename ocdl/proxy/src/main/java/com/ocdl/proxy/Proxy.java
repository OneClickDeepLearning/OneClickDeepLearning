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
import java.util.HashSet;
import java.util.Set;

@Component
public class Proxy implements ProxyCallBack{

    HashMap<String, Set<String>> preModels;
    Set<String> curModel;

    @Resource
    StorageService storage;

    @Resource
    MessageTransferService msgTransfer;


    public static String SOURCE = "/var/lib/jenkins/workspace";
    public static String BUCKETNAME = "ocdl-model";

    public Proxy() {
        // create the preModel and curModel
        preModels = new HashMap<>();
        curModel = new HashSet<>();
    }

//    @Value("${jenkins.server.workspacePath}")
//    public static void setSOURCE(String SOURCE) { Proxy.SOURCE = SOURCE; }
//
//    @Value("${S3.server.bucketName}")
//    public static void setBUCKETNAME(String BUCKETNAME) { Proxy.BUCKETNAME = BUCKETNAME; }

    public void run() {

        System.out.println(SOURCE);
        System.out.println(BUCKETNAME);

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

        System.out.println("SOURTH:" + SOURCE);
        String[] projectInfo = msg.split("_");
        System.out.println("project info: ");
        System.out.println(projectInfo[0] + "   " + projectInfo[1]);
        Path path = Paths.get(SOURCE, projectInfo[0]);

        Set<String> preModel = null;
        System.out.println("PreModel is: ");
        if (preModels.containsKey(projectInfo[0].trim())) {
            preModel = preModels.get(projectInfo[0].trim());
        } else {
            preModel = new HashSet<String>();
            preModels.put(projectInfo[0].trim(), preModel);
        }
        System.out.println(preModel);


        curModel = FileTool.listModel(path.toString());
        System.out.println("CurModel is: ");
        System.out.println(curModel);

        Set<String> newModel = FileTool.getNewModels(curModel, preModel);

        newModel.stream().forEach( v -> {

            File model = new File(Paths.get(path.toString(), v).toString());

            // upload file to S3
            storage.createStorage();
            String modelName = v;
            storage.uploadObject(BUCKETNAME, modelName, model);

            // send message in kafka
            msgTransfer.createProducer();
            String content = modelName + " " + storage.getObkectUrl(BUCKETNAME, modelName);
            msgTransfer.send(projectInfo[1], content);
            System.out.println("produce message send...   ");

        });

        preModel.clear();
        preModel.addAll(curModel);
        curModel.clear();

        System.out.println("=================================================================");
    }


//    private void printModelMap( HashMap<String, Set<String>> models) {
//
//        models.keySet().stream().forEach(key -> {
//            System.out.println(key + ": ");
//            System.out.print(models.get(key));
//            System.out.println();
//        });
//    }




}
