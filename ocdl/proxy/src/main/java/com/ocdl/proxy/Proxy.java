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

    @Resource
    StorageService storage;

    @Resource
    MessageTransferService msgTransfer;
    //TODO 路径放入配置文件
    public static String SOURCE = "/var/lib/jenkins/workspace";
    public static String BUCKETNAME = "ocdl-model";

    public Proxy() {
    }

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

        // parse message to get mdTopic and gitRepoName
        String[] projectInfo = msg.split("_");
        String gitRepoName = projectInfo[0];
        String mdTopic = projectInfo[1];
        Path modelsPath = Paths.get(SOURCE, gitRepoName, "models");

        File models = new File(modelsPath.toString());

        if (models.isDirectory() && models.listFiles().length > 0) {
            File[] modelFiles = models.listFiles();
            for(File model : modelFiles) {
                // upload file to S3
                storage.createStorage();
                storage.uploadObject(BUCKETNAME, model.getName(), model);

                // send message in kafka
                msgTransfer.createProducer();
                String content = model.getName() + " " + storage.getObkectUrl(BUCKETNAME, model.getName());
                msgTransfer.send(mdTopic, content);
                System.out.println("produce message send...   ");
            }
        }
    }
}
