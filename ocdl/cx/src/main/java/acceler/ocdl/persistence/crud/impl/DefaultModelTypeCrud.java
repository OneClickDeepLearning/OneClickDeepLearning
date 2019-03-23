package acceler.ocdl.persistence.crud.impl;

import acceler.ocdl.model.ModelType;
import acceler.ocdl.persistence.crud.ModelTypeCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultModelTypeCrud implements ModelTypeCrud {


    @Override
    public ModelType createModelType(ModelType modelTypeInfo) {
        return null;
    }

    @Override
    public List<ModelType> getModelTypes(Long projectId) {

        return null;
    }

    @Override
    public ModelType findById(Long modelTypeId) {

        return null;
    }




}
