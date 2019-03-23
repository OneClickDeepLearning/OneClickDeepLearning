package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelCrud implements ModelCrud {


    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ProjectCrud projectCrud;


    @Override
    public Model createModel(Model modelInfo) {
        return null;
    }

    @Override
    public Model updateModel(Long id, Model upadataModelInfo) {

        return null;
    }

    @Override
    public List<Model> getModels(Model.Status status) {

        return null;
    }

    @Override
    public Long getBigVersion(Long modelTypeId, Long projectId) {


        return null;
    }

    @Override
    public Long getSmallVersion(Long modelTypeId, Long projectId, Long bigVersion) {

        return null;
    }

    @Override
    public Model getById(Long modelId) {
        return null;

    }
}
