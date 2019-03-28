package acceler.ocdl.persistence;

import acceler.ocdl.model.ModelType;

import java.util.List;

public interface ModelTypeCrud {

    List<String> getModelTypes();

    ModelType getModelType(String modelTypeName);

    int[] getVersion(String modelTypeName);

    void setVersion(String modelTypeName, int bigVersion, int smallVersion);

    void updateModelTypes(String modelTypeInfo);
}
