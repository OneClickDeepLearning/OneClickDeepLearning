package acceler.ocdl.persistence.impl;

import acceler.ocdl.dto.ModelDto;
import acceler.ocdl.model.Model;
import acceler.ocdl.persistence.ModelCrud;
import acceler.ocdl.service.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Service
public class DefaultModelCrud implements ModelCrud {

    @Autowired
    ModelService modelService;

    @Override
    public List<ModelDto> getModels(Model.Status status) {

        return modelService.getModels(status.toString().toLowerCase());
    }

    @Override
    public boolean modelExist(String modelName, String status) {
        return modelService.modelExist(modelName, status);
    }


    @Override
    public boolean moveModel(Path source, Path target) {

        try {
            modelService.moveFile(source, target);
            return true;
        } catch (IOException e) {
            return  false;
        }
    }

}
