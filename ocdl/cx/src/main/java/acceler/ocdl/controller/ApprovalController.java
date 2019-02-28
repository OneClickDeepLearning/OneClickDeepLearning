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
import java.util.Map;

@Controller
@RequestMapping(path = "/approval")
public class ApprovalController {

    @Autowired
    private DatabaseService dbService;

    @ResponseBody
    @RequestMapping(params = "action=getModelList", method = RequestMethod.POST)
    public final Response getModelList(@RequestBody Map<String, String> param) {

        ArrayList<ArrayList<Model>> models = new ArrayList<ArrayList<Model>>();

//        int projectId = Integer.valueOf(param.get("id"));
        int projectId = 3;

        ArrayList<Model> newModels= dbService.getConditioanalProjectModel(projectId, Model.Status.NEW);
        models.add(newModels);
        ArrayList<Model> approvalModels= dbService.getConditioanalProjectModel(projectId, Model.Status.APPROVAL);
        models.add(approvalModels);
        ArrayList<Model> rejectModels= dbService.getConditioanalProjectModel(projectId, Model.Status.REJECT);
        models.add(rejectModels);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(models)
                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=getModeltype", method = RequestMethod.POST)
    public final Response getModeltype(@RequestBody Map<String, String> param) {

        ArrayList<String> modelTypes = new ArrayList<String>();

//        int projectId = Integer.valueOf(param.get("id"));
        int projectId = 3;

        modelTypes = dbService.getModelType(projectId);

        return Response.getBuilder()
                .setCode(Response.Code.SUCCESS)
                .setData(modelTypes)
                .build();
    }

    @ResponseBody
    @RequestMapping(params = "action=pushDecision", method = RequestMethod.POST)
    public final Response pushDecision(@RequestBody Map<String, String> param) {

        Long modelId = Long.parseLong(param.get("id"));
        // -1:new, 0:reject, 1:qpproval
        Model.Status decision = Model.Status.getStatus(Integer.valueOf(param.get("decision")));

        Boolean isSuccess = dbService.updateModelStatusWithModelId(modelId, decision);

        if (isSuccess) {
            return Response.getBuilder()
                    .setCode(Response.Code.SUCCESS)
                    .build();
        } else {
            return Response.getBuilder()
                    .setCode(Response.Code.SUCCESS)
                    .build();
        }
    }

}
