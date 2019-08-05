package acceler.ocdl.controller;


import acceler.ocdl.service.TemplateService;
import acceler.ocdl.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/template")
public final class TemplateController {

    @Autowired
    private TemplateService templateService;

    @ResponseBody
    @RequestMapping(path = "/file", method = RequestMethod.GET)
    public final Response getTemplateFiles(HttpServletRequest request){
        Response.Builder responseBuilder = getBuilder();

        List<List<String>> result = new ArrayList<List<String>>();
        try{
            result.add( templateService.getTemplatesList("Layers"));
            result.add(templateService.getTemplatesList("Blocks"));
            result.add(  templateService.getTemplatesList("Networks"));
            result.add(templateService.getTemplatesList("Frameworks"));
            responseBuilder.setCode(Response.Code.SUCCESS);
            responseBuilder.setData(result);
        }catch (Exception e){
            responseBuilder.setCode(Response.Code.ERROR);
            responseBuilder.setMessage(e.getMessage());
        }


        return responseBuilder.build();
    }

    @ResponseBody
    @RequestMapping(path = "/code", method = RequestMethod.GET)
    public final Response getTemplateCode(@QueryParam("name")String name, @QueryParam("type")String type){
        List<String> templates = new ArrayList <String>();
        templates = templateService.getTemplates2(name,type);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(templates)
                .build();
    }
}
