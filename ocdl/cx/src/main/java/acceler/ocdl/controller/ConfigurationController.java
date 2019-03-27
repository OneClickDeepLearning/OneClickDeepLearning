package acceler.ocdl.controller;


import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.model.Project;
import acceler.ocdl.persistence.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/projects")
public class ConfigurationController {

    @Autowired
    private ProjectCrud projectCrud;

    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(HttpServletRequest request, @RequestBody Map<String, String> projectName) {

        Response.Builder responseBuilder = Response.getBuilder();

        projectCrud.updateProjectName(projectName.get("projectName"));

        return responseBuilder.setCode(Response.Code.SUCCESS).build();

    }


    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProject(HttpServletRequest request, @RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();

        projectCrud.updateProjct(updatedProjectConfig.convert2Project());

        return responseBuilder.setCode(Response.Code.SUCCESS).build();

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getProjectConfig(HttpServletRequest request) {

        Response.Builder responseBuilder = Response.getBuilder();

        Project project = projectCrud.getProjectConfiguration();
        ProjectConfigurationDto projectDto = project.convert2ProjectDto();

        responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(projectDto);

        return responseBuilder.build();
    }
}
