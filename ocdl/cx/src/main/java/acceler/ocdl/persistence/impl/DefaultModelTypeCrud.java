package acceler.ocdl.persistence.impl;


import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.ModelType;
import acceler.ocdl.persistence.ModelTypeCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DefaultModelTypeCrud implements ModelTypeCrud {

    private static final Logger logger = LoggerFactory.getLogger(DefaultModelTypeCrud.class);

    @Autowired
    private Persistence persistence;


    @Override
    public List<String> getModelTypes() {

        List<ModelType>  modelTypes = persistence.getModelTypes();
        List<String> result = new ArrayList<>();
        for (ModelType mt : modelTypes) {
            result.add(mt.getModelTypeName());
        }

        return result;
    }

    @Override
    public ModelType getModelType(String modelTypeName) {
        ModelType modelType = persistence.getModelTypes().stream()
                .filter(mt -> mt.getModelTypeName().equals(modelTypeName))
                .findAny()
                .get();

        if (modelType == null) {
            logger.error("Cannot find such model type!");
            throw new NotFoundException("Cannot find such model type!", "Cannot find such model type!");
        }
        return modelType;
    }


    @Override
    public int[] getVersion(String modelTypeName) {

        ModelType modelType = getModelType(modelTypeName);

        int[] result = new int[2];
        result[0] = modelType.getCurrentBigVersion();
        result[1] = modelType.getCurrentSmallVersion();

        return result;

    }

    @Override
    public void setVersion(String modelTypeName, int bigVersion, int smallVersion) {

        ModelType modelType = getModelType(modelTypeName);

        modelType.setCurrentBigVersion(bigVersion);
        modelType.setCurrentSmallVersion(smallVersion);
    }

    @Override
    public void updateModelTypes(String modelTypeInfo) {

        List<ModelType> modelTypes = persistence.getModelTypes();

        List<String> modelTypeStr = getModelTypes();
        Set<String> preModelTypeSet = new HashSet<>(modelTypeStr);

        Set<String> intersection = new HashSet<>();
        Set<String> newModelTypeSet = new HashSet<>();

        if (modelTypeInfo.indexOf(";") >= 0) {

            String[] modelTypeInfos = modelTypeInfo.split(";");
            for (String mt : modelTypeInfos) {
                if (!mt.trim().equals("")) {
                    newModelTypeSet.add(mt);
                }
            }

            intersection.addAll(newModelTypeSet);
            intersection.retainAll(preModelTypeSet);

            newModelTypeSet.removeAll(preModelTypeSet);


        } else {
            newModelTypeSet.add(modelTypeInfo.trim());

            intersection.addAll(newModelTypeSet);
            intersection.retainAll(preModelTypeSet);

            newModelTypeSet.removeAll(preModelTypeSet);
        }

        if (modelTypes.size() > intersection.size()) {
            Iterator<ModelType> iterator = modelTypes.iterator();
            while (iterator.hasNext()){
                ModelType mt = iterator.next();
                if (!intersection.contains(mt.getModelTypeName())) {
                    iterator.remove();
                }
            }
        }

        if (newModelTypeSet.size() > 0) {
            for (String mt : newModelTypeSet) {
                if (!mt.trim().equals("")) {
                    ModelType modelType = new ModelType();
                    modelType.setModelTypeName(mt);
                    modelType.setCurrentBigVersion(-1);
                    modelType.setCurrentSmallVersion(-1);
                    modelTypes.add(modelType);
                }
            }
        }

        persistence.persistentModelTypes();
    }
}
