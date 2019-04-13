package acceler.ocdl.service;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ModelService {

    /*
        User choose specific models, modify name to "model_name + timestamp" and move them from userspace to /stage/new.
     */
    void initModelToStage(User user);

    void approvalModel(Model model, String algorithmName, Algorithm.UpgradeVersion version);

    void rejectModel(Model model);

    void undo(Model model);

    void pushModeltoGit(String source, String newModelName);

    List<Model> getModelsByStatus(Model.Status status);

    boolean existModel(String modelName, Model.Status status);



}
