package com.ocdl.client.controller;

import com.ocdl.client.service.FileSaveService;
import com.ocdl.client.service.SegmentService;
import com.ocdl.client.service.StorageService;
import com.ocdl.client.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/picture")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    private FileSaveService fileSaveService;

    @Autowired
    private SegmentService segmentService;

    @Autowired
    private StorageService storageService;

    private String success = "success";

    private String bucketName = "ocdl-client";

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response uploadPicture(@RequestParam("file") MultipartFile file) {

        long startTime = System.currentTimeMillis();

        Response.Builder responseBuilder = Response.getBuilder();
        // exception control
        String resultMessage = fileSaveService.saveFile(file);
        if (!resultMessage.equals(success)) {
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(resultMessage);
        }


        //run python to get output picture
        File outputImage = segmentService.run(file.getOriginalFilename());
        if (outputImage == null) {
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage("Fail to run the model.");
        }

        //upload to S3
        String url = storageService.uploadObjectAndGetUrl(bucketName, outputImage.getName(), outputImage);

        long endTime = System.currentTimeMillis();

        // format return data
        Map<String, String> returnData = new HashMap<>();
        returnData.put("url", url);
        returnData.put("eta", Long.toString(endTime-startTime));

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(returnData).build();
    }
}
