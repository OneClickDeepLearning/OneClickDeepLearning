package acceler.ocdl.persistence;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.Model;
import java.nio.file.Path;
import java.util.List;

public interface ModelCrud {

    List<ModelDto> getModels(Model.Status status);

    boolean modelExist(String modelName, String status);

    boolean moveModel(Path source, Path target);
}
