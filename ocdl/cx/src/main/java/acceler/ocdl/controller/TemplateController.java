package acceler.ocdl.controller;


import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.TemplateCategory;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.service.TemplateService;
import acceler.ocdl.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@RequestMapping(path = "/rest/template")
public final class TemplateController {

    @Autowired
    private TemplateService templateService;

    @Autowired
    private ProjectService projectService;

//    @ResponseBody
//    @RequestMapping(path = "/file", method = RequestMethod.GET)
//    public final Response getTemplateFiles(HttpServletRequest request){
//        Response.Builder responseBuilder = getBuilder();
//
//        Map<String,List<String>> result;
//        try{
//            result =  templateService.getTemplatesList();
//            responseBuilder.setCode(Response.Code.SUCCESS);
//            responseBuilder.setData(result);
//        }catch (Exception e){
//            responseBuilder.setCode(Response.Code.ERROR);
//            responseBuilder.setMessage(e.getMessage());
//        }
//        return responseBuilder.build();
//    }
//
//    @ResponseBody
//    @RequestMapping(path = "/code", method = RequestMethod.GET)
//    public final Response getTemplateCode(@QueryParam("name")String name, @QueryParam("type")String type){
//        List<String> templates = new ArrayList <String>();
//        templates = templateService.getCode(name,type);
//
//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(templates)
//                .build();
//    }


    @RequestMapping(path = "/category", method = RequestMethod.GET)
    public Response getCategory(@RequestParam(value = "project_id", required = true) Long projectId){
        Response.Builder responseBuilder = getBuilder();

        Project project = projectService.getProject(projectId);
        TemplateCategory category = templateService.getProjectCategory(project);

        return responseBuilder
                .setCode(Response.Code.SUCCESS)
                .setData(category)
                .build();
    }


    @RequestMapping(path = "/category", method = RequestMethod.POST)
    public Response saveCategory(@RequestBody TemplateCategory templateCategory) {
        Response.Builder responseBuilder = getBuilder();

        TemplateCategory category = templateService.saveCategory(templateCategory);

        return responseBuilder
                .setCode(Response.Code.SUCCESS)
                .setData(category)
                .build();
    }


    @RequestMapping(path = "/category", method = RequestMethod.DELETE)
    public Response deleteCategory(@RequestBody TemplateCategory templateCategory) {
        Response.Builder responseBuilder = getBuilder();

        TemplateCategory category = templateService.saveCategory(templateCategory);

        return responseBuilder
                .setCode(Response.Code.SUCCESS)
                .setData(category)
                .build();
    }



}
