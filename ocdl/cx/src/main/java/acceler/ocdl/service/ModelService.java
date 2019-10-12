package acceler.ocdl.service;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.*;

import java.util.List;
import java.util.Map;

public interface ModelService {

    /**
     * move the model from userspace to stage space, name is formatted at the same time
     * the status of moved model is new, and the format of the fileName = "modelName + timestamp" + "suffix"
     * @param innerUser used for get userspace path, userspacePath = "HDFS path" + "projectName-userID"
     * @return the records of finded, success uploaded, and fail uploaded file
     */
    Map<String, Integer> initModelToStage(InnerUser innerUser);


    /**
     * approval model
     * move model file from folder "new"  to folder "approval" and update the model object
     * the format of the fileName = "modelName" + "timestamp" + "algorithm" + "version" + "suffix"
     * the format of version = "v*.*"
     * @param model model that need to approve
     * @param algorithmName the name of algorithm that chose when approve
     * @param version the version that chose when approve
     */
    void approveModel(NewModel model, String algorithmName, Algorithm.UpgradeVersion version, String comments);

    /**
     * reject model
     * move model file from folder "new"  to folder "reject" and update the model object
     * he format of the fileName = "modelName" + "timestamp" + "suffix"
     * @param model model that need to reject
     */
    void rejectModel(NewModel model, String comments);

    /**
     * undo model
     * move model file from folder "approval" or "reject"  to folder "new" and update the model object
     * the format of the fileName = "modelName" + "timestamp"
     * @param model model that need to undo
     */
    void undo(Model model, String comments);

    /**
     * get models by status
     * @param status specific status
     * @return list of Model
     */
    ModelDto[] getModelsByStatus(Model.Status status);

    /**
     * release model
     * @param model approved model
     */
    void release(ApprovedModel model, InnerUser innerUser);

    Map<String, List<ModelDto>> getModelListByUser(long userId);
}
