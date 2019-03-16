package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.crud.ProjectCrud;
import acceler.ocdl.persistence.dao.ModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelCrud implements ModelCrud {

    @Autowired
    private ModelDao modelDao;

    @Autowired
    private ModelTypeCrud modelTypeCrud;

    @Autowired
    private ProjectCrud projectCrud;


    @Override
    public Model createModel(Model modelInfo) {
        modelInfo.setId(null);
        return modelDao.save(modelInfo);
    }

    @Override
    public Model updateModel(Long id, Model upadataModelInfo) {
        Optional<Model> optionalModel = modelDao.findById(id);
        if (optionalModel.isPresent()) {
            upadataModelInfo.setId(id);
            return modelDao.save(upadataModelInfo);
        }
        return null;
    }

    @Override
    public List<Model> getModels(Model.Status status) {

        return  modelDao.findByStatus(status);
    }

    @Override
    public Long getBigVersion(Long modelTypeId, Long projectId) {

        List<Model> models = modelDao.findByModelTypeIdAndProjectId(modelTypeId, projectId);

        Long biggest = 0L;
        for (Model m : models) {
            if (m.getBigVersion() != null && m.getBigVersion() > biggest) {
                biggest = m.getBigVersion();
            }
        }

        return biggest;
    }

    @Override
    public Long getSmallVersion(Long modelTypeId, Long projectId, Long bigVersion) {

        List<Model> models = modelDao.findByModelTypeIdAndProjectIdAndAndBigVersion(modelTypeId, projectId,bigVersion);

        Long smallest = 0L;
        for (Model m : models) {
            // get the biggest small versrion
            if (m.getSmallVersion() != null && m.getSmallVersion() > smallest) {
                smallest = m.getSmallVersion();
            }
        }

        return smallest;
    }

    @Override
    public Model getById(Long modelId) {
        Optional<Model> modelOption = modelDao.findById(modelId);
        if (modelOption.isPresent()) {
            return modelOption.get();
        } else {
            return null;
        }
    }

}
