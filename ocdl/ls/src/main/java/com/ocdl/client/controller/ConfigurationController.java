package com.ocdl.client.controller;

import com.ocdl.client.service.ConfigurationService;

import com.ocdl.client.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Deprecated
@Controller
@RequestMapping(path = "/rest/config")
public class ConfigurationController {


    private static final Logger logger = LoggerFactory.getLogger(com.ocdl.client.controller.PictureController.class);

    @Autowired
    private ConfigurationService configurationService;


    @ResponseBody
    @RequestMapping(path = "/project", method = RequestMethod.GET)
    public final Response getProjectConfig() {
        Response.Builder responseBuilder = Response.getBuilder();
        if (configurationService.getProjectName() != null && !"".equals(configurationService.getProjectName())) {
            responseBuilder.setData(configurationService.getProjectName());
            responseBuilder.setCode(Response.Code.SUCCESS);
        } else {
            responseBuilder.setCode(Response.Code.ERROR);
            responseBuilder.setMessage("Empty Project Name");
        }
        return responseBuilder.build();
    }

    @ResponseBody
    @RequestMapping(path = "/project", method = RequestMethod.PUT)
    public final Response setProjectConfig(@RequestBody Map<String, String> projectName) {
        Response.Builder responseBuilder = Response.getBuilder();
        Boolean updateResult = configurationService.setProjectName(projectName.get("name"));
        if(updateResult){
            responseBuilder.setCode(Response.Code.SUCCESS);
        }else{
            responseBuilder.setCode(Response.Code.ERROR);
            responseBuilder.setMessage("Fail to change the property");
        }
        return responseBuilder.build();
    }
}

