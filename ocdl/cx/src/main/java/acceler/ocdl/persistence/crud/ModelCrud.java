package acceler.ocdl.persistence.crud;

import acceler.ocdl.model.Model;

import java.util.List;
import java.util.Map;

public interface ModelCrud {


    Model createModel(Model modelInfo);

    Model updateModel(Long id, Model upadataModelInfo);

    List<Model> getModels(Model.Status status);

    Map<String,Long> getVersion(Long modelTypeId, Long projectId);
}
