package acceler.ocdl.service;

import acceler.ocdl.model.Algorithm;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;

import java.util.List;

public interface ModelService {

    /**
     * move the model from userspace to stage space, name is formatted at the same time
     * @param user
     */
    void initModelToStage(User user);


    void approvalModel(Model model, String algorithmName, Algorithm.UpgradeVersion version);

    void rejectModel(Model model);

    void undo(Model model);

    void pushModeltoGit(String source, String newModelName);

    List<Model> getModelsByStatus(Model.Status status);

    boolean existModel(String modelName, Model.Status status);


}
