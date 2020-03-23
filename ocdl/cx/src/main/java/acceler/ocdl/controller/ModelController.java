package acceler.ocdl.controller;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.entity.Model;
import acceler.ocdl.entity.ModelStatus;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.dto.Response;
import acceler.ocdl.service.ProjectService;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static acceler.ocdl.dto.Response.getBuilder;

@RestController
@CrossOrigin
@RequestMapping(path = "/rest/model")
public final class ModelController {
    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ModelService modelService;

    /**
     * Get all models
     * @return
     */
    @RequestMapping(path="/", method = RequestMethod.GET)
    public final Response getModelList(HttpServletRequest request) {
        logger.debug("enter the get model list funciton");
        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        Map<String, Page<Model>> models = new HashMap<>();
        Model newModel = Model.builder()
                .status(ModelStatus.NEW)
                .project(project)
                .build();
        Page<Model> newModels = modelService.getModels(newModel, 0, 10);
        models.put("newModels", newModels);


        Model approvedModel = Model.builder()
                .status(ModelStatus.APPROVED)
                .project(project)
                .build();
        Page<Model> approvedModels = modelService.getModels(approvedModel, 0, 100);
        models.put("approvalModels", approvedModels);

        Model rejectedModel = Model.builder()
                .status(ModelStatus.REJECTED)
                .project(project)
                .build();
        Page<Model> rejectedModels = modelService.getModels(rejectedModel, 0, 100);
        models.put("rejectedModels", rejectedModels);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(models).build();
    }

    /**
     * Get model list by userId
     * @return
     */
    @RequestMapping(path="/event", method = RequestMethod.GET)
    public final Response getModelListByUser(HttpServletRequest request) {
        logger.debug("Get model list by user id");
        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        //Map<String, List<ModelDto>> modelMap = modelService.getModelListByUser(innerUser.getUserId());
        Model model = Model.builder()
                .project(project)
                .owner(user)
                .build();
        Page<Model> models = modelService.getModels(model, 0, 100);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(models)
                .build();
    }
    
    /**
     * The dicision could be approve, reject and undo.
     * @param model model info
     *                 Note: modelDto.status == from;
     *                       modelDto.algorithm is needed only when from=new && to=approval, the value is algorithm name;
     *                       modelDto.version is needed only when from=new && to=approval, the value is "RELEASE_VERSION" or "CACHED_VERSION";
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT)
    public final Response pushDecision(@RequestBody Model model, HttpServletRequest request) {
//        String from = request.getParameter("fromStatus");
//        String to = request.getParameter("toStatus");
//        String upgradeVersion = request.getParameter("upgradeVersion");

        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        Model modelInDb = modelService.getModelById(model.getId());
        if (!modelInDb.getProject().getId().equals(project.getId())) {
            throw new OcdlException("Permission denied!");
        }
        model.setLastOperator(user);
        modelInDb = modelService.updateModel(model);


//        if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.APPROVED.name())) {
//            Model model = NewModel.getNewModelById(Long.parseLong(modelDto.getModelId()))
//                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
//
//            logger.debug("before push decision, the owner id is:" + model.getOwnerId());
//            modelService.approveModel((NewModel) model,modelDto.getAlgorithm(), Algorithm.UpgradeVersion.valueOf(upgradeVersion), modelDto.getComments(), innerUser.getUserId());
//
//        } else if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.REJECTED.name())) {
//            Model model = NewModel.getNewModelById(Long.parseLong(modelDto.getModelId()))
//                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
//            modelService.rejectModel((NewModel) model, modelDto.getComments(), innerUser.getUserId());
//
//        } else if (from.toUpperCase().equals(Model.Status.REJECTED.name()) && to.toUpperCase().equals(Model.Status.NEW.name())) {
//            RejectedModel model = RejectedModel.getRejectedModelById(Long.parseLong(modelDto.getModelId()))
//                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
//            modelService.undo(model, modelDto.getComments(), innerUser.getUserId());
//
//        } else if (from.toUpperCase().equals(Model.Status.APPROVED.name()) && to.toUpperCase().equals(Model.Status.NEW.name())) {
//            Model model = Algorithm.getApprovalModelById(Long.parseLong(modelDto.getModelId()))
//                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
//
//            if (model.getStatus() != Model.Status.RELEASED) {
//                modelService.undo(model, modelDto.getComments(), innerUser.getUserId());
//            } else {
//                throw new OcdlException("Released model cannot undo.");
//            }
//        } else {
//            throw new OcdlException("Invalid From/To parameters.");
//        }
        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(modelInDb)
                .build();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.PATCH)
    public final Response release(@RequestBody Model model, HttpServletRequest request){
        Response.Builder builder = Response.getBuilder();

        Response.Builder responseBuilder = getBuilder();

        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        Model modelInDb = modelService.getModelById(model.getId());
        if (!modelInDb.getProject().getId().equals(project.getId())) {
            throw new OcdlException("Permission denied!");
        }

        modelInDb.setAlgorithm(model.getAlgorithm());
        modelInDb.setCachedVersion(model.getCachedVersion());
        modelInDb.setReleasedVersion(model.getReleasedVersion());

        modelService.release(modelInDb, user);

//        ApprovedModel model = Algorithm.getApprovalModelById(Long.parseLong(modelId))
//                .orElseThrow(()-> new NotFoundException("Fail to found model"));
//        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
//
//        if (model.getStatus() != Model.Status.RELEASED) {
//            modelService.release(model, innerUser);
//        } else {
//            throw new OcdlException("Released model cannot be release again.");
//        }

        return builder.setCode(Response.Code.SUCCESS).build();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response initModelToStage(HttpServletRequest request) {

        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");
        Response.Builder builder = Response.getBuilder();

        Map<String, Integer> initRecords = modelService.initModelToStage(user, project);

        if (initRecords.get("finded") == 0) {
            throw new NotFoundException("No model file founded! ");
        }
        return builder.setCode(Response.Code.SUCCESS).setData(initRecords).build();
    }


    @ResponseBody
    @RequestMapping(path = "/create", method = RequestMethod.POST)
    public final Response createModel(HttpServletRequest request) {

        Response.Builder builder = Response.getBuilder();
        User user = (User) request.getAttribute("CURRENT_USER");
        Project project = (Project) request.getAttribute("PROJECT");

        String refId = CONSTANTS.MODEL_TABLE.MODEL_PREFIX + RandomStringUtils.randomAlphanumeric(CONSTANTS.MODEL_TABLE.LENGTH_REF_ID);

        Model model = Model.builder()
                .name("test")
                .suffix("tflite")
                .status(ModelStatus.NEW)
                .owner(user)
                .lastOperator(user)
                .refId(refId)
                .project(project)
                .build();

        Model modelInDb = modelService.createModel(model);

        return builder.setCode(Response.Code.SUCCESS).setData(modelInDb).build();
    }


}
