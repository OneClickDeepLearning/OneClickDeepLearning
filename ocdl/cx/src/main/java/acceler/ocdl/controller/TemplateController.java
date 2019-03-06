package acceler.ocdl.controller;


import acceler.ocdl.service.TemplateService;
import acceler.ocdl.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/template")
public final class TemplateController {

    @Autowired
    private TemplateService databaseService;

    @ResponseBody
    @RequestMapping(params = "status=getnames", method = RequestMethod.GET)
    public final Response queryTemplatesNames(){
        List<List<String>> result = new ArrayList<List<String>>();
        result.add( databaseService.getTemplatesList("Layers"));
        result.add(databaseService.getTemplatesList("Blocks"));
        result.add(  databaseService.getTemplatesList("Networks"));
        result.add(databaseService.getTemplatesList("Frameworks"));

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(result)
                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=testTemplates", method = RequestMethod.POST)
    public final Response testTemplates(@RequestBody Map<String,String> param){
        List<String> templates = new ArrayList <String>();
        templates = databaseService.getTemplates2(param.get("name"),param.get("type"));

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(templates)
                .build();
    }

    /**
     * example of controller
     * */
    public final Response controllerExample(){
        String reponse = "hello world";
        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .build();
    }
}
