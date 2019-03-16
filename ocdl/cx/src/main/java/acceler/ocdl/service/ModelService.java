package acceler.ocdl.service;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;

import java.util.List;

public interface ModelService {

    public boolean copyModels(User user);

    public boolean pushModel(Model updateModel, String newModelName);
}
