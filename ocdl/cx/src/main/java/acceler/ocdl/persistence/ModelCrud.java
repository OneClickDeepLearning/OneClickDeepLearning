package acceler.ocdl.persistence;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface ModelCrud {

    public List<ModelDto> getModels(Model.Status status);

    boolean modelExist(String modelName, String status);

    boolean moveModel(Path source, Path target);
}
