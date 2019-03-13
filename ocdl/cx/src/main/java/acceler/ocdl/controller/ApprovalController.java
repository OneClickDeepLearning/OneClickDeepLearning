package acceler.ocdl.controller;

import acceler.ocdl.dto.IncomeModelDto;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.dto.ModelTypeDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private ProjectCrud projectCrud;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getModelList(HttpServletRequest request) {

        Response.Builder responseBuilder = Response.getBuilder();
        Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

        Map<String, List<ModelDto>> models = new HashMap<String, List<ModelDto>>();

        try {

            Project project = projectCrud.fineById(projectId);

            List<Model> newModels= modelCrud.getModels(Model.Status.NEW);
            List<ModelDto> newModelDtos = new ArrayList<ModelDto>();
            for (Model m : newModels) {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                newModelDtos.add(m.convert2ModelDto());
            }
            models.put("newModels", newModelDtos);

            List<Model> approvalModels= modelCrud.getModels(Model.Status.APPROVAL);
            List<ModelDto> approvalModelDtos = new ArrayList<ModelDto>();
            approvalModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                approvalModelDtos.add(m.convert2ModelDto());
            });
            models.put("approvalModels", approvalModelDtos);

            List<Model> rejectModels= modelCrud.getModels(Model.Status.REJECT);
            List<ModelDto> rejectModelDtos = new ArrayList<ModelDto>();
            rejectModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                rejectModelDtos.add(m.convert2ModelDto());
            });
            models.put("rejectedModel", rejectModelDtos);

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(models);

        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }

        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/modeltypes", method = RequestMethod.GET)
    public final Response getModeltype(HttpServletRequest request) {

        Response.Builder responseBuilder = Response.getBuilder();
        Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

        try {

            List<ModelType> modelTypes = modelTypeCrud.getModelTypes(projectId);
            List<ModelTypeDto> modelTypeDtos = new ArrayList<ModelTypeDto>();
            modelTypes.stream().forEach(m -> {
                modelTypeDtos.add(m.convert2ModelDto());
            });
            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(modelTypeDtos);
        } catch (Exception e) {
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());
        }

        return responseBuilder.build();
    }


    @ResponseBody
    @RequestMapping(path = "/{modelId}",  method = RequestMethod.PUT)
    public final Response pushDecision(HttpServletRequest request, @PathVariable("modelId") Long modelId, @RequestBody IncomeModelDto incomeModelDto) {
        Response.Builder responseBuilder = Response.getBuilder();

        try {
            Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();
            Model model = incomeModelDto.convert2Model();

            Long bigVersion = 1L;
            Long smallVersion = 0L;

            if (incomeModelDto.getBigVersion() == 1){
                bigVersion = modelCrud.getBigVersion(modelId, projectId) + 1;
            } else {
                bigVersion = modelCrud.getBigVersion(modelId, projectId);
                smallVersion = modelCrud.getSmallVersion(modelId, projectId, bigVersion) + 1;
            }

            model.setBigVersion(bigVersion);
            model.setSmallVersion(smallVersion);

            Model reModel = modelCrud.updateModel(modelId, model);
            reModel.setProject(projectCrud.fineById(reModel.getProjectId()));
            reModel.setModelType(modelTypeCrud.findById(reModel.getModelTypeId()));

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(reModel.convert2ModelDto());

        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        }
        return responseBuilder.build();
    }

}
