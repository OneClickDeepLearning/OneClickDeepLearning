package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.ModelType;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import acceler.ocdl.persistence.dao.ModelTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultModelTypeCrud implements ModelTypeCrud {

    @Autowired
    private ModelTypeDao modelTypeDao;

    @Override
    public ModelType createModelType(ModelType modelTypeInfo) {
        modelTypeInfo.setModelTypeId(null);
        return modelTypeDao.save(modelTypeInfo);
    }

    @Override
    public List<ModelType> getModelTypes(Long projectId) {

        return modelTypeDao.findByProjectId(projectId);
    }

    @Override
    public ModelType findById(Long modelTypeId) {
        Optional<ModelType> optionalModelType = modelTypeDao.findById(modelTypeId);
        if (optionalModelType.isPresent()) {
            return optionalModelType.get();
        }
        return null;
    }

//    @Override
//    public Long getModelTypeId(String modelTypeName) {
//
//        ModelType modelTypes = modelTypeDao.findByName(modelTypeName);
//
//        return modelTypes.getModelTypeId();
//
//    }





}
