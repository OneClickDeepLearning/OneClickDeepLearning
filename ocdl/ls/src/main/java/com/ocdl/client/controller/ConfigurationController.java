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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/config")
public class ConfigurationController {


    private static final Logger logger = LoggerFactory.getLogger(com.ocdl.client.controller.PictureController.class);

    @Autowired
    private FileSaveService fileSaveService;


    @ResponseBody
    @RequestMapping(path = "/project",method = RequestMethod.GET)
    public final Response getProjectConfig() {
        Response.Builder responseBuilder = Response.getBuilder();

        return responseBuilder.build();
    }

}
