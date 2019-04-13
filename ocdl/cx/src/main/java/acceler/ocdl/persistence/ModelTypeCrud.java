package acceler.ocdl.persistence;

import acceler.ocdl.model.Algorithm;
import java.util.List;

public interface ModelTypeCrud {

    List<String> getModelTypes();

    Algorithm getModelType(String modelTypeName);

    int[] getVersion(String modelTypeName);

    void setVersion(String modelTypeName, int bigVersion, int smallVersion);

    void updateModelTypes(String modelTypeInfo);
}
