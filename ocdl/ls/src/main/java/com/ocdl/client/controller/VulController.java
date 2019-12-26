package com.ocdl.client.controller;

import com.ocdl.client.dto.VulDto;
import com.ocdl.client.service.VulService;
import com.ocdl.client.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/vul")
public class VulController {

    @Autowired
    private VulService vulService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response uploadPicture(@RequestBody VulDto vulDto) {

        long startTime = System.currentTimeMillis();

        Response.Builder responseBuilder = Response.getBuilder();

        Response result = vulService.predict(vulDto);

        long endTime = System.currentTimeMillis();

        if (result.getCode() != 200) {
            return responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(result.getMessage()).build();
        }

        // format return data
        Map<String, Object> returnData = new HashMap<>();
        returnData.put("data", result.getData());
        returnData.put("eta", Long.toString(endTime-startTime));

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(returnData).build();

    }
}
