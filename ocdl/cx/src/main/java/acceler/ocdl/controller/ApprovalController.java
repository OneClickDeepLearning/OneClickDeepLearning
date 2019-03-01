package acceler.ocdl.controller;


import acceler.ocdl.model.Model;
import acceler.ocdl.service.DatabaseService;
import acceler.ocdl.utils.Response;
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
    @RequestMapping(params = "action=getModelList", method = RequestMethod.POST)
    public final Map<String, ArrayList<Model>> getModelList(@RequestBody Map<String, String> param) {

//        ArrayList<ArrayList<Model>> models = new ArrayList<ArrayList<Model>>();
        Map<String, ArrayList<Model>> models = new HashMap<String, ArrayList<Model>>();

//        int projectId = Integer.valueOf(param.get("id"));
        int projectId = 3;

        dbService.createConn();
        ArrayList<Model> newModels= dbService.getConditioanalProjectModel(projectId, Model.Status.NEW);
        models.put("newModel", newModels);


        ArrayList<Model> approvalModels= dbService.getConditioanalProjectModel(projectId, Model.Status.APPROVAL);
        models.put("approvalModel", approvalModels);

        ArrayList<Model> rejectModels= dbService.getConditioanalProjectModel(projectId, Model.Status.REJECT);
        models.put("rejectedModel", rejectModels);

        return models;

//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(models)
//                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=getModeltype", method = RequestMethod.POST)
    public final Map<Integer, String> getModeltype(@RequestBody Map<String, String> param) {

        Map<Integer, String> result = new HashMap<Integer, String>();

        ArrayList<String> modelTypes = new ArrayList<String>();

//        int projectId = Integer.valueOf(param.get("id"));
        int projectId = 3;

        dbService.createConn();
        modelTypes = dbService.getModelType(projectId);

        for (int i=0; i< modelTypes.size(); i++) {
            result.put(i, modelTypes.get(i));
        }

        return result;

//        return Response.getBuilder()
//                .setCode(Response.Code.SUCCESS)
//                .setData(modelTypes)
//                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=updateDecision", method = RequestMethod.POST)
    public final Map<String, String> pushDecision(@RequestBody Map<String, String> param) {

        Map<String, String> result = new HashMap<String, String>();

        Long modelId = Long.parseLong(param.get("id"));
        // -1:new, 0:reject, 1:qpproval
        Model.Status decision = Model.Status.getStatus(Integer.valueOf(param.get("decision")));
        dbService.createConn();
        Boolean isSuccess = dbService.updateModelStatusWithModelId(modelId, decision);

        if (isSuccess) {

            result.put("isSuccess", "true");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        } else {
            result.put("isSuccess", "false");
//            return Response.getBuilder()
//                    .setCode(Response.Code.SUCCESS)
//                    .build();
        }
        return result;
    }

}
