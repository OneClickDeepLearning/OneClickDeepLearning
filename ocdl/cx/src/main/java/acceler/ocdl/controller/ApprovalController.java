package acceler.ocdl.controller;

import acceler.ocdl.dto.IncomeModelDto;
import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.dto.ModelTypeDto;
import acceler.ocdl.dto.Response;
import acceler.ocdl.exception.DatabaseException;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.User;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import acceler.ocdl.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static acceler.ocdl.dto.Response.*;


@Controller
@RequestMapping(path = "/rest/models")
public class ApprovalController {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalController.class);

    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ModelCrud modelCrud;

    @Autowired
    private ProjectCrud projectCrud;

    @Autowired
    private ModelService modelService;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public final Response getModelList(HttpServletRequest request) {
        Builder responseBuilder = getBuilder();
        long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

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
   /*             newModelDtos.add(m.convert2ModelDto());*/
            }

            List<Model> newModels= modelCrud.getModels(Model.Status.NEW, projectId);
            List<ModelDto> newModelDtos = convert2modelDtos(newModels, project);
            models.put("newModels", newModelDtos);
            List<Model> approvalModels= modelCrud.getModels(Model.Status.APPROVAL);
            List<ModelDto> approvalModelDtos = new ArrayList<ModelDto>();
            approvalModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
/*                approvalModelDtos.add(m.convert2ModelDto());*/
            });
            models.put("approvalModels", approvalModelDtos);

            List<Model> rejectModels= modelCrud.getModels(Model.Status.REJECT);
            List<ModelDto> rejectModelDtos = new ArrayList<ModelDto>();
            rejectModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
       /*         rejectModelDtos.add(m.convert2ModelDto());*/
            });
            models.put("rejectedModels", rejectModelDtos);

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(models);

        } catch (NotFoundException e) {
            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());

        } 

        return responseBuilder.build();
    }

    private List<ModelDto> convert2modelDtos(List<Model> models, Project project) {

        List<ModelDto> modelDtos = new ArrayList<>();
        models.stream().filter(m -> m.getModelTypeId() != -1)
                .forEach(m -> m.setModelType(modelTypeCrud.findById(m.getModelTypeId())));

        models.forEach(m -> {
            m.setProject(project);
            modelDtos.add(m.convert2ModelDto());
        });

        return modelDtos;
    }

    @ResponseBody
    @RequestMapping(path = "/modeltypes", method = RequestMethod.GET)
    public final Response getModeltype(HttpServletRequest request) {

        Builder responseBuilder = getBuilder();
        long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

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
    public final Response pushDecision(HttpServletRequest request, @PathVariable("modelId") long modelId, @RequestBody IncomeModelDto incomeModelDto) {
        Builder responseBuilder = getBuilder();

        try {
            long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();
            Model updateModel = modelCrud.getById(modelId);
            if (updateModel == null) {
                String msg = String.format("Model(id: %s ) not found", modelId);
                String responseMsg = String.format("Model(id: %s ) not found", modelId);
                throw new NotFoundException(msg, responseMsg);
            }
            updateModel.setId(modelId);
            updateModel.setModelTypeId(incomeModelDto.getModelTypeId());

            if (incomeModelDto.getStatus().equals("Approval")) {
                updateModel.setStatus(Model.Status.APPROVAL);

                logger.debug("--Model push start--");
                modelService.pushModel(updateModel,getNewModelName(updateModel));
                logger.debug("--Model push finished--");

            } else if (incomeModelDto.getStatus().equals("Reject")) {
                updateModel.setStatus(Model.Status.REJECT);
            } else {
                updateModel.setStatus(Model.Status.NEW);
            }
            
            long bigVersion = 1L;
            long smallVersion = 0L;
            
            if (incomeModelDto != null && incomeModelDto.getBigVersion() >= 0) {
                
                if (incomeModelDto.getBigVersion() == 1){
                    long currentBigVersion = modelCrud.getBigVersion(modelId, projectId);
                    if (currentBigVersion >= 0) {
                        bigVersion = currentBigVersion + 1;
                    } else {
                        bigVersion = 1L;
                    }
                    smallVersion = 0L;

                } else {
                    long currentBigVersion = modelCrud.getBigVersion(modelId, projectId);

                    if (currentBigVersion >= 0){
                        bigVersion = currentBigVersion;
                    } else {
                        currentBigVersion = 0L;
                    }

                    long currentSmallVersion = modelCrud.getSmallVersion(modelId, projectId, bigVersion);

                    if (currentSmallVersion >= 0) {
                        smallVersion = modelCrud.getSmallVersion(modelId, projectId, bigVersion) + 1;
                    } else {
                        smallVersion = 1L;
                    }
                }
            }

            updateModel.setBigVersion(bigVersion);
            updateModel.setSmallVersion(smallVersion);
            
            Model updatedModel = modelCrud.updateModel(modelId, updateModel);
            updatedModel.setProject(projectCrud.fineById(updatedModel.getProjectId()));
            updatedModel.setModelType(modelTypeCrud.findById(updatedModel.getModelTypeId()));

      /*      responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(reModel.convert2ModelDto());*/
        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());
        }

        return responseBuilder.build();
    }

    private String getNewModelName(Model updateModel){
        StringBuilder newModelName = new StringBuilder();

        ModelType modelType = modelTypeCrud.findById(updateModel.getModelTypeId());
        if (modelType == null) {
            String msg = String.format("Model Type (id: %s ) not found", updateModel.getModelTypeId());
            String responseMsg = String.format("Model Type (id: %s ) not found", updateModel.getModelTypeId());
            throw new NotFoundException(msg, responseMsg);
        }

        newModelName.append(modelType.getName());
        newModelName.append("_v");
        newModelName.append(String .valueOf(updateModel.getBigVersion()));
        newModelName.append(".");
        newModelName.append(String .valueOf(updateModel.getSmallVersion()));
        return newModelName.toString();
    }

}
