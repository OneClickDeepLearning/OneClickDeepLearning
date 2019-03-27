package acceler.ocdl.service;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.Model;
import acceler.ocdl.model.User;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ModelService {

    public boolean copyModels(User user);

    public boolean pushModel(String source, String newModelName);

    List<ModelDto> getModels(String status);

    boolean modelExist(String modelName, String preStatus);

    boolean moveFile(Path source, Path target) throws IOException;
}
