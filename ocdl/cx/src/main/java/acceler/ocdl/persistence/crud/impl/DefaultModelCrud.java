package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.persistence.crud.ModelCrud;
import acceler.ocdl.persistence.dao.ModelDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DefaultModelCrud implements ModelCrud {

    @Autowired
    private ModelDao modelDao;

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
        return modelDao.findByStatus(status);
    }

    @Override
    public Map<String,Long> getVersion(Long modelTypeId, Long projectId) {

        List<Model> models = modelDao.findByModelTypeIdAndProjectId(modelTypeId, projectId);

        // 0 - big, 1- small
        Map<String,Long> version = new HashMap<String,Long>();
        version.put("big", 0L);
        version.put("small", 0L);
        for (Model m : models) {
            if (m.getBigVersion() > version.get("big")) version.put("big", m.getBigVersion());
            if (m.getSmallVersion() > version.get("small")) version.put("big", m.getSmallVersion());
        }

        return version;
    }





}
