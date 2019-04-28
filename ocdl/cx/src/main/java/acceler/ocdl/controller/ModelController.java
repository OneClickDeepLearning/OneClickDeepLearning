package acceler.ocdl.controller;

import acceler.ocdl.OcdlApplication;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;
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
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/model")
public final class ModelController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private ModelService modelService;

    private String newStr = "new";
    private String approvalStr = "approval";
    private String rejectStr = "reject";

    /**
     * Get all models
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getModelList() {

        logger.debug("enter the get model list funciton");
        Response.Builder responseBuilder = getBuilder();

        Map<String, List<ModelDto>> models = new HashMap<>();
        List<Model> newModels= modelService.getModelsByStatus(Model.Status.NEW);
        models.put("newModels", convert2ListModelDto(newModels));

        List<Model> approvedModels= modelService.getModelsByStatus(Model.Status.APPROVED);
        models.put("approvalModels", convert2ListModelDto(approvedModels));

        List<Model> rejectedModels= modelService.getModelsByStatus(Model.Status.REJECTED);
        models.put("rejectedModels", convert2ListModelDto(rejectedModels));

        responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(models);

        return responseBuilder.build();
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
    @RequestMapping(path = "/{status}",  method = RequestMethod.PUT)
    public final Response pushDecision(@RequestBody ModelDto modelDto,
                                       @QueryParam("fromStatus")String from, @QueryParam("toStatus")String to) {

        logger.debug("enter the get model list funciton +++++++++++++++++");
        Response.Builder responseBuilder = getBuilder();

        if (from.equals(newStr) && to.equals(approvalStr)) {
            modelService.approvalModel(modelDto.convertToModel(),modelDto.getAlgorithm(), Algorithm.UpgradeVersion.valueOf(modelDto.getVersion()));
        } else if (from.equals(newStr) && to.equals(rejectStr)) {
            modelService.rejectModel(modelDto.convertToModel());
        } else if ((from.equals(approvalStr) || from.equals(toString())) && to.equals(newStr)) {
            modelService.undo(modelDto.convertToModel());
        } else {
            throw new OcdlException("Invalid From/To parameters.");
        }

        responseBuilder.setCode(Response.Code.SUCCESS);

        return responseBuilder.build();

    }


    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response initModelToStage(HttpServletRequest request) {
        User user = (User) request.getAttribute("CURRENT_USER");
        Response.Builder builder = Response.getBuilder();

        modelService.initModelToStage(user);

        builder.setCode(Response.Code.SUCCESS);
        return builder.build();
    }


    private List<ModelDto> convert2ListModelDto(List<Model> modelList) {

        List<ModelDto> modelDtoList = new ArrayList<>();

        modelList.forEach(model -> {
            ModelDto modelDto = model.convertToModelDto(model);
            modelDtoList.add(modelDto);
        });

        return modelDtoList;
    }

}
