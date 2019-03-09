package acceler.ocdl.controller;


import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;
import acceler.ocdl.model.Model;
import acceler.ocdl.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/approval")
public class ApprovalController {

    @Autowired
    private DatabaseService dbService;

    @ResponseBody
    @RequestMapping(path = "/model", method = RequestMethod.GET)
    public final Response getModelList() {

        Response.Builder responseBuilder = Response.getBuilder();

        Map<String, ArrayList<Model>> models = new HashMap<String, ArrayList<Model>>();
        int projectId = 3;

        try {
            ArrayList<Model> newModels= dbService.getConditioanalProjectModel(projectId, Model.Status.NEW);
            models.put("newModel", newModels);


            ArrayList<Model> approvalModels= dbService.getConditioanalProjectModel(projectId, Model.Status.APPROVAL);
            models.put("approvalModel", approvalModels);

            ArrayList<Model> rejectModels= dbService.getConditioanalProjectModel(projectId, Model.Status.REJECT);
            models.put("rejectedModel", rejectModels);

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(models);

        } catch (DatabaseException e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }

        return responseBuilder.build();
    }

    @ResponseBody
    @RequestMapping(path = "modeltype", method = RequestMethod.GET)
    public final Response getModeltype() {

        Response.Builder responseBuilder = Response.getBuilder();

        ArrayList<String> modelTypes = new ArrayList<String>();
        int projectId = 3;

        try {
            modelTypes = dbService.getModelType(projectId);
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(modelTypes);

        } catch (DatabaseException e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/model/decision", method = RequestMethod.PUT)
    public final Response pushDecision(@RequestBody ModelDto modelDto) {
        Response.Builder responseBuilder = Response.getBuilder();

        int projectId = 3;

        try {
            int modelTypeId = dbService.getModelTypeId(projectId, modelDto.getModelType());
            int statusId = dbService.getStatusId(modelDto.getStatus());

            int bigVersion = dbService.getLatestBigVersion(projectId, modelTypeId);
            int smallVersion = dbService.getLatestSmallVersion(projectId, modelTypeId);

            if (modelDto.getIsbigVersion() == 1) {
                bigVersion ++;
                smallVersion = 0;
            } else {
                smallVersion++;
            }

            dbService.updateModelStatusWithModelId(modelDto.getModelId(), modelTypeId, statusId, bigVersion, smallVersion);
            responseBuilder.setCode(Response.Code.SUCCESS);

        } catch (DatabaseException e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }

        return responseBuilder.build();
    }

}
