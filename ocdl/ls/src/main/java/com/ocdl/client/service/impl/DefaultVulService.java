package com.ocdl.client.service.impl;

import com.google.gson.Gson;
import com.ocdl.client.Client;
import com.ocdl.client.dto.VulDto;
import com.ocdl.client.service.HttpRequestService;
import com.ocdl.client.service.VulService;
import com.ocdl.client.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;

@Service
public class DefaultVulService implements VulService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultKafkaConsumerService.class);

    @Autowired
    private HttpRequestService httpRequestService;

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

    private Gson gson = new Gson();

    @Override
    public Response predict(VulDto vulDto) {

        String currentModelName = "owasp_randomForest_model.sav";

        String modelPath = Paths.get(WORKSPACEPATH, MODELBASEPATH, currentModelName).toString();

        String body = gson.toJson(vulDto);
        logger.info("Send request to Flask server: " + body);
        String response = httpRequestService.post(FLASKSEVERURL, body);

        Response responseObj = gson.fromJson(response, Response.class);

        return responseObj;
    }
}
