package com.ocdl.client.controller;

import com.ocdl.client.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(path = "/rest/picture")
public class PictureController {

    private static final Logger logger = LoggerFactory.getLogger(PictureController.class);

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response uploadPicture() {

        logger.debug("enter the upload funciton +++++++++++++++++");

        Response.Builder responseBuilder = Response.getBuilder();

        //TODO func details

        responseBuilder.setCode(Response.Code.SUCCESS);

        return responseBuilder.build();
    }
}
