package acceler.ocdl.persistence.crud;

import acceler.ocdl.model.ModelType;

import java.util.List;

public interface ModelTypeCrud {

    ModelType createModelType(ModelType modelTypeInfo);

    List<ModelType> getModelTypes(Long projectId);

    ModelType findById(Long modelTypeId);

//    Long getModelTypeId(String modelTypeName);
}


