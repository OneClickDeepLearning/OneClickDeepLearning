package acceler.ocdl.controller;

import acceler.ocdl.dto.IncomeModelDto;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.dto.ModelTypeDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(path = "/models")
public class ApprovalController {

    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ModelCrud modelCrud;

//    @ResponseBody
//    @RequestMapping(path = "/{projectId}", method = RequestMethod.GET)
//    public final Response getModelList(@PathVariable("projectId") Long projectId) {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        Map<String, List<ModelDto>> models = new HashMap<String, List<ModelDto>>();
//
//        try {
//            List<Model> newModels= modelCrud.getModels(Model.Status.NEW);
//            List<ModelDto> newModelDtos = new ArrayList<ModelDto>();
//            for (Model m : newModels) {
//                newModelDtos.add(m.convert2ModelDto());
//            }
//            models.put("newModels", newModelDtos);
//
//            List<Model> approvalModels= modelCrud.getModels(Model.Status.APPROVAL);
//            List<ModelDto> approvalModelDtos = new ArrayList<ModelDto>();
//            newModels.stream().forEach(m -> {
//                approvalModelDtos.add(m.convert2ModelDto());
//            });
//            models.put("approvalModels", approvalModelDtos);
//
//            List<Model> rejectModels= modelCrud.getModels(Model.Status.REJECT);
//            List<ModelDto> rejectModelDtos = new ArrayList<ModelDto>();
//            newModels.stream().forEach(m -> {
//                rejectModelDtos.add(m.convert2ModelDto());
//            });
//            models.put("rejectedModel", rejectModelDtos);
//
//            responseBuilder.setCode(Response.Code.SUCCESS)
//                    .setData(models);
//
//        } catch (Exception e) {
//
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//
//        }
//
//        return responseBuilder.build();
//    }


//    @ResponseBody
//    @RequestMapping(path = "/{projectId}/modeltypes", method = RequestMethod.GET)
//    public final Response getModeltype(@PathVariable("projectId") Long projectId) {
//
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        try {
//
//            List<ModelType> modelTypes = modelTypeCrud.getModelTypes(projectId);
//            List<ModelTypeDto> modelTypeDtos = new ArrayList<ModelTypeDto>();
//            modelTypes.stream().forEach(m -> {
//                modelTypeDtos.add(m.convert2ModelDto());
//            });
//            responseBuilder.setCode(Response.Code.SUCCESS)
//                    .setData(modelTypeDtos);
//        } catch (Exception e) {
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//        }
//
//        return responseBuilder.build();
//    }


//    @ResponseBody
//    @RequestMapping(path = "/{projectId}/{modelId}",  method = RequestMethod.PUT)
//    public final Response pushDecision(@PathVariable("projectId") Long projectId, @PathVariable("modelId") Long modelId, @RequestBody IncomeModelDto incomeModelDto) {
//        Response.Builder responseBuilder = Response.getBuilder();
//
//        try {
//
//            Model model = incomeModelDto.convert2Model();
//
//            Map<String,Long> version = new HashMap<String,Long>();
//            if (incomeModelDto.getStatus() == Model.Status.APPROVAL.toString()) {
//                version = modelCrud.getVersion(incomeModelDto.getModelTypeId(), projectId);
//            }
//
//            model.setBigVersion(version.get("big"));
//            model.setSmallVersion(version.get("small"));
//
//            modelCrud.updateModel(modelId, model);
//            responseBuilder.setCode(Response.Code.SUCCESS);
//
//        } catch (Exception e) {
//
//            responseBuilder.setCode(Response.Code.ERROR)
//                    .setMessage(e.getMessage());
//
//        }
//        return responseBuilder.build();
//    }

}
