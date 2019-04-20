package com.ocdl.client.controller;

import com.ocdl.client.service.FileSaveService;
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

@Controller
@RequestMapping(path = "/rest/picture")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @Autowired
    private FileSaveService fileSaveService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response uploadPicture(@RequestParam("file") MultipartFile file) {

        Response.Builder responseBuilder = Response.getBuilder();
        String resultMessage = fileSaveService.saveFile(file);
        if (resultMessage.equals("success")) {



            responseBuilder.setCode(Response.Code.SUCCESS);
        } else {

            responseBuilder.setCode(Response.Code.ERROR);
            responseBuilder.setData(resultMessage);
        }

        return responseBuilder.build();
    }
}
