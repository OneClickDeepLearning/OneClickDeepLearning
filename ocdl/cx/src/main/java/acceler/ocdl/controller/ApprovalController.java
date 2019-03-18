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
import acceler.ocdl.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(path = "/rest/models")
public class ApprovalController {

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
        Response.Builder responseBuilder = Response.getBuilder();
        Long projectId = ((User)request.getAttribute("CURRENT_USER")).getProjectId();

        Map<String, List<ModelDto>> models = new HashMap<String, List<ModelDto>>();

        try {
            //FIXME: 所有数据库的select必须check null,再做操作, throw NotFoundException
            Project project = projectCrud.fineById(projectId);

            List<Model> newModels= modelCrud.getModels(Model.Status.NEW, projectId);
            List<ModelDto> newModelDtos = new ArrayList<ModelDto>();
            for (Model m : newModels) {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    //FIXME: @Query in repo interface
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                newModelDtos.add(m.convert2ModelDto());
            }
            models.put("newModels", newModelDtos);

            List<Model> approvalModels= modelCrud.getModels(Model.Status.APPROVAL, projectId);
            List<ModelDto> approvalModelDtos = new ArrayList<ModelDto>();
            //FIXME: list.forEach()
            //FIXME: .filter() : != null
            approvalModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                approvalModelDtos.add(m.convert2ModelDto());
            });
            models.put("approvalModels", approvalModelDtos);

            List<Model> rejectModels= modelCrud.getModels(Model.Status.REJECT, projectId);
            List<ModelDto> rejectModelDtos = new ArrayList<ModelDto>();
            //FIXME: encapsulate method
            rejectModels.stream().forEach(m -> {
                m.setProject(project);
                if (m.getModelTypeId() != null) {
                    m.setModelType(modelTypeCrud.findById(m.getModelTypeId()));
                }
                rejectModelDtos.add(m.convert2ModelDto());
            });
            models.put("rejectedModels", rejectModelDtos);

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(models);

            //FIXME: not allow to catch Exception here, specific Exception
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
            Model updateModel = modelCrud.getById(modelId);
            //FIXME: check null
            updateModel.setId(modelId);
            updateModel.setModelTypeId(incomeModelDto.getModelTypeId());

            if (incomeModelDto.getStatus().equals("Approval")) {
                updateModel.setStatus(Model.Status.APPROVAL);

                //System.out.println("[debug]" + updateModel.getStatus());
                //FIXME:String.format("kubernete exception: %s", message)
                //FIXME: 每个string都是个储存在永生代的对象, GC 很伤
                modelService.pushModel(updateModel,getNewModelName(updateModel));

                System.out.println("[debug]" + "continue");


            } else if (incomeModelDto.getStatus().equals("Reject")) {
                updateModel.setStatus(Model.Status.REJECT);
            } else {
                updateModel.setStatus(Model.Status.NEW);
            }

            //FIXME: long 还是 Long, GC 很伤， 没用必要wrapped obj 就不要
            Long bigVersion = 1L;
            Long smallVersion = 0L;

            //FIXME: dto 防止 null,且 .getBigVersion() 也可能为 null, 这样它会自动变成0，不符合业务逻辑
            //FIXME: if(incomeModelDto && incomeModelDto.getBigVersion() && incomeModelDto.getBigVersion() == 1)
            if (incomeModelDto.getBigVersion() == 1){
                bigVersion = modelCrud.getBigVersion(modelId, projectId) + 1;
            } else {
                bigVersion = modelCrud.getBigVersion(modelId, projectId);
                smallVersion = modelCrud.getSmallVersion(modelId, projectId, bigVersion) + 1;
            }

            updateModel.setBigVersion(bigVersion);
            updateModel.setSmallVersion(smallVersion);
            //FIXME: naming 不怕长， 怕没自解释性， 我看了就不知道reModel 代表啥
            Model reModel = modelCrud.updateModel(modelId, updateModel);
            reModel.setProject(projectCrud.fineById(reModel.getProjectId()));
            reModel.setModelType(modelTypeCrud.findById(reModel.getModelTypeId()));

            responseBuilder.setCode(Response.Code.SUCCESS)
                    .setData(reModel.convert2ModelDto());
            //FIXME: remove Exception
        } catch (Exception e) {

            responseBuilder.setCode(Response.Code.ERROR)
                    .setMessage(e.getMessage());
        }
        return responseBuilder.build();
    }

    private String getNewModelName(Model updateModel){
        StringBuilder newModelName = new StringBuilder();

        //FIXME: 数据库读取都必须防止null, 如果数据库没该id, 马上就NPE
        newModelName.append(modelTypeCrud.findById(updateModel.getModelTypeId()).getName());
        newModelName.append("_v");
        newModelName.append(updateModel.getBigVersion().toString());
        newModelName.append(".v");
        newModelName.append(updateModel.getSmallVersion().toString());
        return newModelName.toString();
    }

}
