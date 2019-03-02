package acceler.ocdl.controller;


import acceler.ocdl.model.Project;
import acceler.ocdl.service.ConfigurationService;
import acceler.ocdl.service.DatabaseService;
import acceler.ocdl.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/configuration")
public class ConfigurationController {

//    @Autowired
//    private ConfigurationService configurationService;
//
//    @ResponseBody
//    @RequestMapping(params = "action=project", method = RequestMethod.GET)
//    public final Response queryProjectNames() {
//        List<String> result = new ArrayList<>();
//        result.add(configurationService.RequestProjectName());
//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(result)
//                .build();
//    }
//
//    @ResponseBody
//    @RequestMapping(params = "action=changeProjectName", method = RequestMethod.POST)
//    public final Response changeProjectNames(@RequestBody Map<String, String> param) {
//        List<String> result = new ArrayList<>();
//        configurationService.update("project.name", param.get("name"));
//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(result)
//                .build();
//    }

    @Autowired
    private DatabaseService dbService;

    @ResponseBody
    @RequestMapping(params = "action=updateProjectName", method = RequestMethod.POST)
    public final  Map<String, String> updateProjectNames(@RequestBody Map<String, String> param) {

        Map<String, String> result = new HashMap<String, String>();

        dbService.createConn();
        boolean isSuccess = dbService.setProjectName( param.get("name"), 3);

        if (isSuccess) {
            result.put("isSuccess", "true");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        } else {
            result.put("isSuccess", "false");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        }
        return result;
    }


    @ResponseBody
    @RequestMapping(params = "action=getAllProject", method = RequestMethod.POST)
    public final Map<Integer, Project> getAllProject() {

        Map<Integer, Project> result = new HashMap<Integer, Project>();

        // TODO user_id should be te param
        Long user_id = 1L;

        dbService.createConn();
        ArrayList<Project> projects = dbService.getProjectList(user_id);

        projects.stream().forEach((p -> {
            result.put(p.getProjectId(), p);
        }));

        return result;

//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(projects)
//                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=updateProject", method = RequestMethod.POST)
    public final Map<String, String> updateProject(@RequestBody Map<String, String> param) {

        // TODO: projectId should be a param
        Map<String, String> result = new HashMap<String, String>();

        dbService.createConn();
        boolean isSuccess = dbService.updateProject(3, param.get("name"), param.get("git"), param.get("k8"), param.get("template"));

        if (isSuccess) {
            result.put("isSuccess", "true");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        } else {
            result.put("isSuccess", "false");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        }
        return result;
    }












}
