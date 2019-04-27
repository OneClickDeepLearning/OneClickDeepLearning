package acceler.ocdl.controller;

import acceler.ocdl.dto.IncomeModelDto;
import acceler.ocdl.dto.ModelDto;
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
import javax.ws.rs.QueryParam;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static acceler.ocdl.dto.Response.getBuilder;

@Controller
@RequestMapping(path = "/rest/model")
public final class ModelController {

    private static final Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private ModelService modelService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getModelList() {

        logger.debug("enter the get model list funciton +++++++++++++++++");
        Response.Builder responseBuilder = getBuilder();

        Map<String, List<ModelDto>> models = new HashMap<>();

        List<ModelDto> newModels= modelCrud.getModels(Model.Status.NEW);
        models.put("newModels", newModels);

        List<ModelDto> approvalModels= modelCrud.getModels(Model.Status.APPROVAL);
        models.put("approvalModels", approvalModels);

        List<ModelDto> rejectModels= modelCrud.getModels(Model.Status.REJECT);
        models.put("rejectedModels", rejectModels);

        responseBuilder.setCode(Response.Code.SUCCESS)
                .setData(models);

        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/{modelId}",  method = RequestMethod.PUT)
    public final Response pushDecision(@PathVariable("modelId") String modelId, @RequestBody IncomeModelDto incomeModelDto,
                                       @QueryParam("from") String from, @QueryParam("to")String to) {

        logger.debug("enter the get model list funciton +++++++++++++++++");
        Response.Builder responseBuilder = getBuilder();

        // if corresponding model file exit
        if (! modelCrud.modelExist(modelId, incomeModelDto.getDestStatus())) {
            logger.error("Cannot find the model File");
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage("Cannot find the model File");
        }

        String stagePath = "/home/ec2-user/stage/";
        Path source = Paths.get(stagePath, incomeModelDto.getPreStatus(), modelId);

        String newModelName = getNewModelName(modelId, incomeModelDto, source);
        Path target = Paths.get(stagePath, incomeModelDto.getDestStatus(), newModelName);

        boolean success = modelCrud.moveModel(source, target);

        if (success == true) {

            responseBuilder.setCode(Response.Code.SUCCESS);

        } else {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage("Fail to move file");
        }

        return responseBuilder.build();

    }

    private String getNewModelName(String modelId, IncomeModelDto incomeModelDto, Path source) {

        String suffix = "";
        String modelName = "";
        // remove suffix
        int posDot = modelId.lastIndexOf(".");
        if (posDot >= 0) {
            suffix = modelId.substring(posDot);
            modelName = modelId.substring(0, posDot);
        }

        String[] modelInfo = modelName.split("_");

        StringBuilder newModelName = new StringBuilder();
        // when new or reject, the file name will be FN_TS.suffix
        newModelName.append(modelInfo[0]);
        newModelName.append("_");

        long time = new Date().getTime();
        newModelName.append(String.valueOf(time));
        newModelName.append("_");

        // when approval, the file name will be FN_TS_MT_V*.*.suffix
        if (incomeModelDto.getDestStatus().equals("approval")){
            newModelName.append(incomeModelDto.getModelType());

            int bigVersion = 1;
            int smallVersion = 0;


            int[] currentVersion = modelTypeCrud.getVersion(incomeModelDto.getModelType());
            int currentBigVersion = currentVersion[0];
            int currentSmallVersion = currentVersion[1];


            if (incomeModelDto.getBigVersion() == 1) {

                if (currentBigVersion >= 0) {
                    bigVersion = currentBigVersion + 1;
                } else {
                    bigVersion = 1;
                }
                smallVersion = 0;

            } else {

                if (currentBigVersion >= 0){
                    bigVersion = currentBigVersion;
                } else {
                    currentBigVersion = 0;
                }

                if (currentSmallVersion >= 0) {
                    smallVersion = currentSmallVersion + 1;
                } else {
                    smallVersion = 1;
                }
            }

            modelTypeCrud.setVersion(incomeModelDto.getModelType(), bigVersion, smallVersion);
            newModelName.append("_v");
            newModelName.append(bigVersion);
            newModelName.append(".");
            newModelName.append(smallVersion);


            logger.debug("befor push model to git");
            String newPushName = getNewPushedModelName(incomeModelDto.getModelType(), String.valueOf(bigVersion), String.valueOf(smallVersion), suffix);
            modelService.pushModel(source.toString(), newPushName);
        }

        newModelName.append(suffix);

        return newModelName.toString();
    }

    private String getNewPushedModelName(String modelType, String bigVersion, String smallVersion, String suffix ){
        StringBuilder newModelName = new StringBuilder();

        newModelName.append(modelType);
        newModelName.append("_v");
        newModelName.append(bigVersion);
        newModelName.append(".");
        newModelName.append(smallVersion);
        newModelName.append(suffix);

        return newModelName.toString();
    }


    /*
      move model from user space to stage
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public final Response queryPushModels(HttpServletRequest request) {
        User user = (User) request.getAttribute("CURRENT_USER");
        Response.Builder builder = Response.getBuilder();

        if(modelService.copyModels(user)) {
            builder.setCode(Response.Code.SUCCESS)
                    .setData("copy succeeded");
        } else{
            builder.setCode(Response.Code.ERROR)
                    .setMessage("copy failed");
        }
        return builder.build();
    }



}
