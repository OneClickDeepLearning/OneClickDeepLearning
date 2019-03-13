package acceler.ocdl.controller;


import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

@Controller
@RequestMapping(path = "/projects")
public class ConfigurationController {

    @Autowired
    private ProjectCrud projectCrud;

    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(HttpServletRequest request, @RequestBody Map<String, String> projectName) {

        Response.Builder responseBuilder = Response.getBuilder();
        Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

        try{
            Project project = new Project();
            project.setProjectName(projectName.get("projectName"));

            Project reProject = projectCrud.updateProjectName(projectId, project);

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(reProject.convert2ProjectDto());

        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }


    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProject(HttpServletRequest request, @RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();
        Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

        try{
            Project updatedProject = updatedProjectConfig.convert2Project();

            Project reProject = projectCrud.updateProjct(projectId, updatedProject);
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(reProject.convert2ProjectDto());

        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getAllProject() {

        Response.Builder responseBuilder = Response.getBuilder();

//        // TODO userId should be get in context
//        Long userId = 1L;

        try{

            Project project = projectCrud.fineById(3L);
            ProjectConfigurationDto projectDto = project.convert2ProjectDto();

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(projectDto);

        } catch (Exception e) {
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());
        }

        return responseBuilder.build();
    }
}
