package acceler.ocdl.persistence.crud;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;

import java.util.List;
import java.util.Map;

public interface ModelCrud {

//    Model updateModel(Long id, Model upadataModelInfo);

    public List<Model> getModels(Model.Status status);

    public int getBigVersion(ModelType modelType);

    public int getSmallVersion(ModelType modelType);

    public Model getById(long modelId);
}
