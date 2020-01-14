package acceler.ocdl.controller;

import acceler.ocdl.dto.Response;
import acceler.ocdl.entity.Algorithm;
import acceler.ocdl.entity.Project;
import acceler.ocdl.service.AlgorithmService;
import acceler.ocdl.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;

import java.util.List;

import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@RequestMapping(path = "/rest/project")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private AlgorithmService algorithmService;

    @RequestMapping(path = "/algorithm/get", method = RequestMethod.POST)
    public Response getAlgorithm(@RequestBody Algorithm algorithm,
                                       @RequestParam(value = "page", required = false, defaultValue = "0") int page ,
                                       @RequestParam(value = "size", required = false, defaultValue = "10") int size) {

        Response.Builder responseBuilder = getBuilder();

        Page<Algorithm> algorithmPage = algorithmService.getAlgorithm(algorithm, page, size);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(algorithmPage)
                .build();
    }

    @RequestMapping(path = "/algorithm", method = RequestMethod.POST)
    public Response saveAlgorithm(@RequestBody Algorithm algorithm) {

        Response.Builder responseBuilder = getBuilder();

        Algorithm algorithmInDb = algorithmService.saveAlgorithm(algorithm);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(algorithmInDb)
                .build();
    }


    @RequestMapping(path = "/algorithm", method = RequestMethod.DELETE)
    public Response batchDeleteAlgorithm(@RequestBody List<Algorithm> algorithms) {

        Response.Builder responseBuilder = getBuilder();

        boolean success = algorithmService.batchDeleteAlgorithm(algorithms);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(success)
                .build();
    }



//    @ResponseBody
//    @RequestMapping(path = "/latest/{algorithm}", method = RequestMethod.GET)
//    public final Response getLatestModelName(@PathVariable String algorithm) {
//
//        Response.Builder responseBuilder = getBuilder();
//
//        String latestModelName = algorithmService.getLatestModelName(algorithm);
//
//        responseBuilder.setCode(Response.Code.SUCCESS)
//                .setData(latestModelName);
//        return responseBuilder.build();
//    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public final Response getProjectConfig(@RequestParam(value = "id", required = true) Long id) {

        Response.Builder responseBuilder = Response.getBuilder();

        Project project = projectService.getProject(1L);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(project)
                .build();
    }


    @RequestMapping(path = "/config", method = RequestMethod.PUT)
    public Response saveProject(@RequestBody Project project) {

        Response.Builder responseBuilder = Response.getBuilder();
        Project projectInDb = projectService.saveProject(project);
        //algorithmService.updateAlgorithmList(updatedProjectConfig.getAlgorithmStrList(), updatedProjectConfig.getForceRemoved());

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(projectInDb)
                .build();
    }


    @RequestMapping(path = "", method = RequestMethod.DELETE)
    public Response deleteProject(@RequestBody Project project) {

        Response.Builder responseBuilder = getBuilder();

        boolean success = projectService.deleteProject(project.getId());

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(success)
                .build();
    }




//    @RequestMapping(path = "/config/name", method = RequestMethod.PUT)
//    @ResponseBody
//    public Response updateProjectNames(@RequestBody Map<String, String> projectName) {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//        String name = projectName.get("name");
//
//        if (!StringUtil.isNullOrEmpty(name)) {
//            Project project = new Project();
//            project.setProjectName(projectName.get("name"));
//            projectService.updateProjectConfiguration(project);
//            responseBuilder.setCode(Response.Code.SUCCESS)
//                    .setData(projectName);
//        } else {
//            responseBuilder.setCode(Response.Code.ERROR).setMessage("ProjectName can not be empty");
//        }
//        return responseBuilder.build();
//    }

}
