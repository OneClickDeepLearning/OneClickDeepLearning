package acceler.ocdl.controller;


import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;
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

    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(@RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();

        try{
            // one manager - one project, project Id is redundant
            dbService.setProjectName(updatedProjectConfig.getProjectName(), 3);
            responseBuilder.setCode(Response.Code.SUCCESS);

        } catch (DatabaseException e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }


    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProject(@RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();

        try{
            int projectId = dbService.getProjectId(updatedProjectConfig.getProjectName());

            if (projectId == -1 ) {
                projectId = dbService.createProject(updatedProjectConfig.getProjectName());
            }

            dbService.updateProject(projectId, updatedProjectConfig.getProjectName(), updatedProjectConfig.getGitUrl(), updatedProjectConfig.getK8Url(), updatedProjectConfig.getTemplatePath());
            responseBuilder.setCode(Response.Code.SUCCESS);

        } catch (DatabaseException e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }

}
