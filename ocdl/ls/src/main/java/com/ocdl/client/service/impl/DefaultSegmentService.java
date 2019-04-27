package com.ocdl.client.service.impl;


import com.ocdl.client.Client;
import com.ocdl.client.service.SegmentService;
import com.ocdl.client.util.CommandHelper;
import com.ocdl.client.util.DefaultCmdHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;

@Service
public class DefaultSegmentService implements SegmentService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    @Value("${workspace.path}")
    private String WORKSPACEPATH;

    @Value("${models.path}")
    private String MODELBASEPATH;

    @Value("${pictures.path}")
    private String PICBASEPATH;

    @Value("${segmentation.path}")
    private String SEGPICBASEPATH;

    @Value("${ground.truth.path}")
    private String GROUNDTRUTHBASEPATH;

    @Autowired
    private CommandHelper commandHelper;


    public File run(String pictureName) {

        StringBuilder cmd = new StringBuilder();
        cmd.append("python3 ");
        cmd.append(Paths.get(MODELBASEPATH,"end_tflite.py"));
        cmd.append(" ");
        cmd.append(Paths.get(MODELBASEPATH, "unet_membrane.tflite"));
        cmd.append(" ");
        cmd.append(Paths.get(PICBASEPATH, pictureName));

        //System.out.println(pictureName);
        String outputPictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_seg.png";
        //System.out.println(outputPictureName);
        cmd.append(" ");
        cmd.append(Paths.get(SEGPICBASEPATH, outputPictureName));

        String groundTruthName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_segmentation.png";
        System.out.println(groundTruthName);
        cmd.append(" ");
        cmd.append(Paths.get(GROUNDTRUTHBASEPATH, groundTruthName));

        System.out.println(cmd.toString());
        commandHelper.runCommand(new File(WORKSPACEPATH),cmd.toString());


        return new File(Paths.get(SEGPICBASEPATH, outputPictureName).toString());
    }





}
