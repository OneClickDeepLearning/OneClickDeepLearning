package acceler.ocdl.controller;


import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.model.Project;
import acceler.ocdl.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/project")
public class ConfigurationController {

    @Autowired
    private DatabaseService dbService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Response createNewProject(@RequestBody ProjectConfigurationDto projectConfig) {
        Response.Builder responseBuilder = Response.getBuilder();

        /*
        try{
            projectService.create(projectConfig);
            responseBuilder.setCode(Response.Code.SUCCESS);
        }catch (ProjectCreateException exp){
            responseBuilder.setCode(Response.Code.ERROR);
            responseBuilder.setMessage(e.getMessage());
        }
        */

        return responseBuilder.build();
    }

    //FIXME: 请遵守restful 规范，修改用 put 表示，  url表示资源对象
    //FIXME: controller 的 requestbody 如果有必要必须使用 dto 概念
    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(@RequestBody ProjectConfigurationDto updatedProjectConfig) {

        //FIXME：数据传输层接口不应该出现在 controller 层
        //FIXME：delete dbService.createConn();

        boolean isSuccess = dbService.setProjectName(param.get("name"), 3);

        //FIXME: 仔细想想为何我使用构造链模式， 这个例子很能体现
        Response.Builder responseBuilder = Response.getBuilder();

        if (isSuccess) {
            responseBuilder.setCode(Response.Code.SUCCESS);
        } else {
            responseBuilder.setCode(Response.Code.ERROR);
            //FIXME: 为什么失败了， 下层服务通过自定义的exception 来让controller层知道， 注意这个必须是 checkedException
            responseBuilder.setMessage("");
        }
        return responseBuilder.build();
    }

    //FIXME: get all list: url 设计 : /project GET, restful 标准
    @RequestMapping(params = "action=getAllProject", method = RequestMethod.POST)
    @ResponseBody
    public Map<Integer, Project> getAllProject() {

        Map<Integer, Project> result = new HashMap<Integer, Project>();

        // TODO user_id should be te param
        //FIXME: 后端的参数命名规范 userId
        Long user_id = 1L;

        dbService.createConn();
        ArrayList<Project> projects = dbService.getProjectList(user_id);

        //FIXME: ArrayList.forEach(Consumer), 不需要先转stream()
        projects.stream().forEach((p -> {
            result.put(p.getProjectId(), p);
        }));

        return result;

//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(projects)
//                .build();
    }


    @RequestMapping(params = "action=updateProject", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> updateProject(@RequestBody Map<String, String> param) {

        // TODO: projectId should be a param
        Map<String, String> result = new HashMap<String, String>();

        dbService.createConn();
        //FIXME: 多参数情况，使用DTO
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
