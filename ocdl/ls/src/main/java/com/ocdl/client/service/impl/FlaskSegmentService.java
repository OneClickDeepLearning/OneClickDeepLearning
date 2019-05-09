package com.ocdl.client.service.impl;

import com.ocdl.client.service.SegmentService;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


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

//        LinkedMultiValueMap body = new LinkedMultiValueMap();
//        body.add("model_path", modelPath);
//        body.add("test_pic_path", picturePath);
//        body.add("output_image_path", outputPath);
//        body.add("ground_truth_path", groundTruthPath);
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
//        HttpEntity entity = new HttpEntity(body, headers);
//
//        ResponseEntity<String> responds = restTemplate.exchange(FLASKSEVERURL, HttpMethod.POST, entity, String.class);
//        System.out.println("============================================");
//        System.out.println(responds.getBody());

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(FLASKSEVERURL);

            // set post request
            post.setHeader("Content-type", "application/json");
            String body = String.format("{\"model_path\":\"%s\", \"test_pic_path\":\"%s\", \"output_image_path\":\"%s\", \"ground_truth_path\":\"%s\"}", modelPath, picturePath, outputPath, groundTruthPath);
            StringEntity bodyEntity = new StringEntity(body);
            post.setEntity(bodyEntity);

            // excute post request
            HttpResponse response = client.execute(post);
            System.out.println("\nSending 'POST' request to URL : " + FLASKSEVERURL);
            System.out.println("Post parameters : " + post.getEntity());
            System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            System.out.println(result.toString());
        }catch (IOException e) {
            System.out.println("IOException: " + e);
        }

        return new File(Paths.get(SEGPICBASEPATH, basePictureName + "_seg.png").toString());
    }
}
