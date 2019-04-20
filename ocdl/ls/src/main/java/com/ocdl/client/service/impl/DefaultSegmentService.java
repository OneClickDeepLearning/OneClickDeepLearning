package com.ocdl.client.service.impl;


import com.ocdl.client.service.SegmentService;
import com.ocdl.client.util.CmdHelper;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class DefaultSegmentService implements SegmentService {

    private final String MODELBASEPATH = getClass().getResource("/models/lesion_segmentation").getPath();
    private final String PICBASEPATH = getClass().getResource("/pictures").getPath();
    private final String SEGPICBASEPATH = getClass().getResource("/pictures_segmentation").getPath();


    public String run(String pictureName) {

        StringBuilder cmd = new StringBuilder();
        cmd.append("python3 ");
        cmd.append(Paths.get(MODELBASEPATH,"end_tflite.py"));
        cmd.append(" ");
        cmd.append(Paths.get(MODELBASEPATH, "unet_membrane.tflite"));
        cmd.append(" ");
        cmd.append(Paths.get(PICBASEPATH, pictureName));

        System.out.println(pictureName);
        String outputPictureName = pictureName.substring(0, pictureName.lastIndexOf(".")) + "_seg.png";
        cmd.append(" ");
        cmd.append(Paths.get(SEGPICBASEPATH, outputPictureName));

        String result = CmdHelper.runCommand(cmd.toString());
        System.out.println("The result is: ");
        System.out.println(result.isEmpty());
        System.out.println(result);

        return outputPictureName;
    }





}
