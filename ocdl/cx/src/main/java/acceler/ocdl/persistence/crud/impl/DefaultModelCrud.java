package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelCrud implements ModelCrud {


//    @Autowired
//    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ProjectCrud projectCrud;


//    @Override
//    public Model createModel(Model modelInfo) {
//        return null;
//    }
//
//    @Override
//    public Model updateModel(Long id, Model upadataModelInfo) {
//
//        return null;
//    }

    @Override
    public List<Model> getModels(Model.Status status) {
        return null;
    }

    @Override
    public int getBigVersion(ModelType modelType) {

        return modelType.getCurrentBigVersion();
    }

    @Override
    public int getSmallVersion(ModelType modelType) {

        return modelType.getCurrentSmallVersion();
    }

    @Override
    public Model getById(long modelId, List<Model> models) {
        return null;
    }
}
