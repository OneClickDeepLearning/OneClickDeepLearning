package com.ocdl.proxy.controller;

import com.ocdl.proxy.util.CmdHelper;
import com.ocdl.proxy.util.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.file.Path;
import java.nio.file.Paths;


@Controller
@RequestMapping(path = "/laucher")
public final class SetUpController {

    @ResponseBody
    @RequestMapping(path="/{projectName}", params = "action=setuplaucher", method = RequestMethod.POST)
    public final Response setUpLaucher(@PathVariable("projectName") String projectName){

        System.out.println("This is the Laucher........................");
        Path path= Paths.get("/home/ec2-user/OneClickDLTemp/ocdl/proxy/src/main/resources");

        CmdHelper.runCommand("laucher.sh", projectName, path.toString());


        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .build();
    }
}
