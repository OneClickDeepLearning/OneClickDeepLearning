//package acceler.ocdl.controller;
//
//
//import acceler.ocdl.dto.ProjectConfigurationDto;
//import acceler.ocdl.dto.Response;
//import acceler.ocdl.exception.DatabaseException;
//import acceler.ocdl.crud.DatabaseService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.ArrayList;
//
//@Controller
//@RequestMapping(path = "/projects")
//public class ConfigurationController {
//
//    @Autowired
//    private DatabaseService dbService;
//
//    @RequestMapping(path = "/{projectId}/config/name", method = RequestMethod.PUT)
//    @ResponseBody
//    public Response updateProjectNames(@PathVariable("projectId") Long projectId, @RequestBody Map<String, String> projectName) {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        try{
//            // one manager - one project, project Id is redundant
//            // projectId = 3
//            dbService.setProjectName(projectName.get("name"), projectId);
//            responseBuilder.setCode(Response.Code.SUCCESS);
//
//        } catch (DatabaseException e) {
//
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//
//        }
//        return responseBuilder.build();
//    }
//
//
//    @RequestMapping(path = "/{projectId}/config", method = RequestMethod.PUT)
//    @ResponseBody
//    public Response updateProject(@PathVariable("projectId") Long projectId, @RequestBody ProjectConfigurationDto updatedProjectConfig) {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        try{
//
//            dbService.updateProject(projectId, updatedProjectConfig.getProjectName(), updatedProjectConfig.getGitUrl(), updatedProjectConfig.getK8Url(), updatedProjectConfig.getTemplatePath());
//            responseBuilder.setCode(Response.Code.SUCCESS);
//
//        } catch (DatabaseException e) {
//
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//
//        }
//        return responseBuilder.build();
//    }
//
//
//    @ResponseBody
//    @RequestMapping(method = RequestMethod.GET)
//    public final Response getAllProject() {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        // TODO userId should be get in context
//        Long userId = 1L;
//
//        try{
//            ArrayList<ProjectConfigurationDto> projects = dbService.getProjectList(userId);
//            responseBuilder.setCode(Response.Code.SUCCESS)
//                    .setData(projects);
//
//        } catch (DatabaseException e) {
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//        }
//
//        return responseBuilder.build();
//    }
//}
