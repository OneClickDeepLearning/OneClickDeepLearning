package acceler.ocdl.controller;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.*;
import acceler.ocdl.service.ModelService;
import acceler.ocdl.dto.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/model")
public final class ModelController {
    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private ModelService modelService;

    /**
     * Get all models
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getModelList() {
        logger.debug("enter the get model list funciton");
        Response.Builder responseBuilder = getBuilder();

        Map<String, ModelDto[]> models = new HashMap<>();
        ModelDto[] newModels= modelService.getModelsByStatus(Model.Status.NEW);
        models.put("newModels", newModels);

        ModelDto[] approvedModels= modelService.getModelsByStatus(Model.Status.APPROVED);
        models.put("approvalModels", approvedModels);

        ModelDto[] rejectedModels= modelService.getModelsByStatus(Model.Status.REJECTED);
        models.put("rejectedModels", rejectedModels);

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(models).build();
    }

    /**
     * Get model list by userId
     * @return
     */
    @ResponseBody
    @RequestMapping(path="/event", method = RequestMethod.GET)
    public final Response getModelListByUser(HttpServletRequest request) {
        logger.debug("Get model list by user id");
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        Response.Builder responseBuilder = getBuilder();

        Map<String, List<ModelDto>> modelMap = modelService.getModelListByUser(innerUser.getUserId());

        return responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(modelMap).build();
    }
    
    /**
     * The dicision could be approve, reject and undo.
     * @param modelDto model info
     *                 Note: modelDto.status == from;
     *                       modelDto.algorithm is needed only when from=new && to=approval, the value is algorithm name;
     *                       modelDto.version is needed only when from=new && to=approval, the value is "RELEASE_VERSION" or "CACHED_VERSION";
     * @return
     */
    @ResponseBody
    @RequestMapping(path="/{modelId}", method = RequestMethod.POST)
    public final Response pushDecision(@RequestBody ModelDto modelDto, HttpServletRequest request) {
        String from = request.getParameter("fromStatus");
        String to = request.getParameter("toStatus");
        String upgradeVersion = request.getParameter("upgradeVersion");
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");

        Response.Builder responseBuilder = getBuilder();

        if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.APPROVED.name())) {
            Model model = NewModel.getNewModelById(Long.parseLong(modelDto.getModelId()))
                    .orElseThrow(()-> new NotFoundException("Fail to found model"));

            logger.debug("before push decision, the owner id is:" + model.getOwnerId());
            modelService.approveModel((NewModel) model,modelDto.getAlgorithm(), Algorithm.UpgradeVersion.valueOf(upgradeVersion), modelDto.getComments(), innerUser.getUserId());

        } else if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.REJECTED.name())) {
            Model model = NewModel.getNewModelById(Long.parseLong(modelDto.getModelId()))
                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
            modelService.rejectModel((NewModel) model, modelDto.getComments(), innerUser.getUserId());

        } else if (from.toUpperCase().equals(Model.Status.REJECTED.name()) && to.toUpperCase().equals(Model.Status.NEW.name())) {
            RejectedModel model = RejectedModel.getRejectedModelById(Long.parseLong(modelDto.getModelId()))
                    .orElseThrow(()-> new NotFoundException("Fail to found model"));
            modelService.undo(model, modelDto.getComments(), innerUser.getUserId());
            
        } else if (from.toUpperCase().equals(Model.Status.APPROVED.name()) && to.toUpperCase().equals(Model.Status.NEW.name())) {
            Model model = Algorithm.getApprovalModelById(Long.parseLong(modelDto.getModelId()))
                    .orElseThrow(()-> new NotFoundException("Fail to found model"));

            if (model.getStatus() != Model.Status.RELEASED) {
                modelService.undo(model, modelDto.getComments(), innerUser.getUserId());
            } else {
                throw new OcdlException("Released model cannot undo.");
            }
        } else {
            throw new OcdlException("Invalid From/To parameters.");
        }
        return responseBuilder.setCode(Response.Code.SUCCESS).build();
    }


    @ResponseBody
    @RequestMapping(path="/{modelId}", method = RequestMethod.PATCH)
    public final Response release(@PathVariable String modelId, HttpServletRequest request){
        Response.Builder builder = Response.getBuilder();

        ApprovedModel model = Algorithm.getApprovalModelById(Long.parseLong(modelId))
                .orElseThrow(()-> new NotFoundException("Fail to found model"));
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");

        if (model.getStatus() != Model.Status.RELEASED) {
            modelService.release(model, innerUser);
        } else {
            throw new OcdlException("Released model cannot be release again.");
        }

        return builder.setCode(Response.Code.SUCCESS).build();
    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response initModelToStage(HttpServletRequest request) {
        String result;
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        Response.Builder builder = Response.getBuilder();

        Map<String, Integer> initRecords = modelService.initModelToStage(innerUser);

        if (initRecords.get("finded") == 0) {
            throw new NotFoundException("No model file founded! ");
        }
        return builder.setCode(Response.Code.SUCCESS).setData(initRecords).build();
    }


}
