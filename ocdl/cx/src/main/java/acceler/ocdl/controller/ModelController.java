package acceler.ocdl.controller;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.NewModel;
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
import javax.ws.rs.QueryParam;
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
     * The dicision could be approve, reject and undo.
     * @param modelDto model info
     *                 Note: modelDto.status == from;
     *                       modelDto.algorithm is needed only when from=new && to=approval, the value is algorithm name;
     *                       modelDto.version is needed only when from=new && to=approval, the value is "RELEASE_VERSION" or "CACHED_VERSION";
     * @param from from status
     * @param to to status
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.PUT)
    public final Response pushDecision(@RequestBody ModelDto modelDto,
                                       @QueryParam("fromStatus")String from, @QueryParam("toStatus")String to) {

        logger.debug("enter the get model list funciton +++++++++++++++++");
        Response.Builder responseBuilder = getBuilder();

        logger.debug("from: " + from);
        logger.debug("to: " + to);
        logger.debug(Model.Status.NEW.name());

        if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.APPROVED.name())) {
            modelService.approveModel((NewModel) modelDto.convertToModel(),modelDto.getAlgorithm(), Algorithm.UpgradeVersion.valueOf(modelDto.getVersion()));
        } else if (from.toUpperCase().equals(Model.Status.NEW.name()) && to.toUpperCase().equals(Model.Status.REJECTED.name())) {
            modelService.rejectModel((NewModel) modelDto.convertToModel());
        } else if ((from.toUpperCase().equals(Model.Status.APPROVED.name()) || from.toUpperCase().equals(Model.Status.REJECTED.name())) && to.toUpperCase().equals(Model.Status.NEW.name())) {
            modelService.undo(modelDto.convertToModel());
        } else {
            throw new OcdlException("Invalid From/To parameters.");
        }

        return responseBuilder.setCode(Response.Code.SUCCESS).build();

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response initModelToStage(HttpServletRequest request) {
        InnerUser innerUser = (InnerUser) request.getAttribute("CURRENT_USER");
        Response.Builder builder = Response.getBuilder();

        modelService.initModelToStage(innerUser);

        return builder.setCode(Response.Code.SUCCESS).build();
    }


}
