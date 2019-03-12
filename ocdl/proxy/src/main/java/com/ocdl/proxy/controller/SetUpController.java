package com.ocdl.proxy.controller;

import com.ocdl.proxy.util.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path = "/laucher")
public final class SetUpController {

    @ResponseBody
    @RequestMapping(params = "action=setuplaucher", method = RequestMethod.POST)
    public final Response setUpLaucher(){

        System.out.println("This is the Laucher........................");


        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .build();
    }
}
