package acceler.ocdl.service;

import acceler.ocdl.entity.Model;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ModelService {

    /**
     * move the model from userspace to stage space, name is formatted at the same time
     * the status of moved model is new, and the format of the fileName = "modelName + timestamp" + "suffix"
     * @param user used for get userspace path, userspacePath = "HDFS path" + "projectName-userID"
     * @return the records of finded, success uploaded, and fail uploaded file
     */
    Map<String, Integer> initModelToStage(User user, Project project);


    /**
     * approval model
     * move model file from folder "new"  to folder "approval" and update the model object
     * the format of the fileName = "modelName" + "timestamp" + "algorithm" + "version" + "suffix"
     * the format of version = "v*.*"
     * @param model model that need to approve
     * @param algorithmName the name of algorithm that chose when approve
     */
    void approveModel(Model model, String algorithmName, String comments, User lastOperator);

    /**
     * reject model
     * move model file from folder "new"  to folder "reject" and update the model object
     * he format of the fileName = "modelName" + "timestamp" + "suffix"
     * @param model model that need to reject
     */
    void rejectModel(Model model, String comments, User lastOperator);

    /**
     * undo model
     * move model file from folder "approval" or "reject"  to folder "new" and update the model object
     * the format of the fileName = "modelName" + "timestamp"
     * @param model model that need to undo
     */
    void undo(Model model, String comments, User lastOperator);


    /**
     * release model
     * @param model approved model
     */
    Model release(Model model, User user);

    Model createModel(Model model);

    Model updateModel(Model model);

    Boolean deleteModel(Model model);

    Page<Model> getModels(Model model, int page, int size);

    Model getModelById(Long id);
}
