package acceler.ocdl.controller;

import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.model.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ProjectCrud projectCrud;

    @ResponseBody
    @RequestMapping(path = "/algorithm", method = RequestMethod.GET)
    public final Response getAlgorithm() {

        logger.debug("enter the get model types funciton +++++++++++++++++");

        Response.Builder responseBuilder = getBuilder();

        List<String> modelTypes = modelTypeCrud.getModelTypes();

        responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(modelTypes);

        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/config", method = RequestMethod.GET)
    public final Response getProjectConfig() {

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

    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProject(@RequestBody ProjectConfigurationDto updatedProjectConfig) {

        Response.Builder responseBuilder = Response.getBuilder();

        projectCrud.updateProject(updatedProjectConfig.convert2Project());

        modelTypeCrud.updateModelTypes(updatedProjectConfig.getModelTypes());

        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }



    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
    @ResponseBody
    public Response updateProjectNames(@RequestBody Map<String, String> projectName) {

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

    @RequestMapping(path = "/data", method = RequestMethod.POST)
    @ResponseBody
    public Response uploadData() {

        Response.Builder responseBuilder = Response.getBuilder();
        return responseBuilder.build();
    }

}
