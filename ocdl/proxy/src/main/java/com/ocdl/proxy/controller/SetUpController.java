package com.ocdl.proxy.controller;

import com.ocdl.proxy.service.JenkinsService;
import com.ocdl.proxy.service.KafkaTopicService;
import com.ocdl.proxy.util.CmdHelper;
import com.ocdl.proxy.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequestMapping(path = "/laucher")
public final class SetUpController {

    @Autowired
    JenkinsService jenkinsService;

    @Autowired
    KafkaTopicService kafkaTopicService;

    @ResponseBody
    @RequestMapping(path="/{projectName}", params = "action=setuplaucher", method = RequestMethod.POST)
    public final Response setUpLaucher(@PathVariable("projectName") String projectName){

        Response.Builder builder = Response.getBuilder();

        try{

            System.out.println("This is the Laucher........................");
            Path path= Paths.get("/home/ec2-user/OneClickDLTemp/ocdl/proxy/src/main/resources");

            CmdHelper.runCommand("laucher.sh", projectName, path.toString());
            String gitUrl = "http://ec2-54-89-140-122.compute-1.amazonaws.com/git/" + projectName;

            String topic = projectName+"jkmsg";
            kafkaTopicService.createTopic(topic);

            String outputFileName = topic + ".txt";
            jenkinsService.generateXML(projectName, gitUrl, topic, outputFileName);

            builder.setCode(Response.Code.SUCCESS);

        } catch (Exception e) {
            builder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return builder.build();
    }
}
