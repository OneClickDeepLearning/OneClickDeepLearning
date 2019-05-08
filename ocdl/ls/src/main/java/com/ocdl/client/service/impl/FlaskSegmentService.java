package com.ocdl.client.service.impl;

import com.ocdl.client.service.SegmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.file.Paths;


@Service
public class FlaskSegmentService implements SegmentService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    @Value("${flask_sever_url}")
    private String FLASKSEVERURL;

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

    @Override
    public File run(String pictureName) {

        String modelPath = Paths.get(WORKSPACEPATH, MODELBASEPATH, "unet_membrane.tflite").toString();
        String picturePath = Paths.get(WORKSPACEPATH, PICBASEPATH, pictureName).toString();

        String basePictureName = pictureName.substring(0, pictureName.lastIndexOf("."));

        String outputPath = Paths.get(WORKSPACEPATH,SEGPICBASEPATH, basePictureName + "_seg.png").toString();
        String groundTruthPath = Paths.get(WORKSPACEPATH,GROUNDTRUTHBASEPATH, basePictureName + "_segmentation.png").toString();

        LinkedMultiValueMap body = new LinkedMultiValueMap();
        body.add("model_path", modelPath);
        body.add("test_pic_path", picturePath);
        body.add("output_image_path", outputPath);
        body.add("ground_truth_path", groundTruthPath);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity entity = new HttpEntity(body, headers);

        ResponseEntity<String> responds = restTemplate.exchange(FLASKSEVERURL, HttpMethod.POST, entity, String.class);
        System.out.println("============================================");
        System.out.println(responds.getBody());

        return new File(Paths.get(SEGPICBASEPATH, basePictureName + "_seg.png").toString());
    }
}
