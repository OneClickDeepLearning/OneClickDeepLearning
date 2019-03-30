package acceler.ocdl.controller;


import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;;
import acceler.ocdl.persistence.ModelTypeCrud;
import acceler.ocdl.persistence.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/rest/projects")
public class ConfigurationController {

    @Autowired
    private ProjectCrud projectCrud;

    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(HttpServletRequest request, @RequestBody Map<String, String> projectName) {

        Response.Builder responseBuilder = Response.getBuilder();

        projectCrud.updateProjectName(projectName.get("projectName"));

        try {
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(projectName);

        }catch (Exception e){
            responseBuilder.setCode(Response.Code.ERROR)
                    .setData(e);
        }
        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }


    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProject(HttpServletRequest request, @RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();

        projectCrud.updateProject(updatedProjectConfig.convert2Project());

        modelTypeCrud.updateModelTypes(updatedProjectConfig.getModelTypes());

        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getProjectConfig(HttpServletRequest request) {

        Response.Builder responseBuilder = Response.getBuilder();

        Project project = projectCrud.getProjectConfiguration();
        ProjectConfigurationDto projectDto = project.convert2ProjectDto();

        List<String> modelTypes = modelTypeCrud.getModelTypes();

        StringBuilder modelTypeBuilder = new StringBuilder();
        modelTypes.forEach(mt -> {
            modelTypeBuilder.append(mt);
            modelTypeBuilder.append("; ");
        });

        projectDto.setModelTypes(modelTypeBuilder.toString());

        responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(projectDto);

        return responseBuilder.build();
    }
}
