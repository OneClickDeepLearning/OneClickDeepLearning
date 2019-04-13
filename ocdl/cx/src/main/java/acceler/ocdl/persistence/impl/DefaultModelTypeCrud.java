package acceler.ocdl.persistence.impl;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Algorithm;
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
        List<Algorithm>  modelTypes = persistence.getModelTypes();
        List<String> result = new ArrayList<>();
        for (Algorithm mt : modelTypes) {
            result.add(mt.getAlgorithmName());
        }
        return result;
    }

    @Override
    public Algorithm getModelType(String modelTypeName) {
        Optional<Algorithm> modelType = persistence.getModelTypes().stream()
                .filter(mt -> mt.getAlgorithmName().equals(modelTypeName))
                .findAny();

        if (!modelType.isPresent()) {
            logger.error("Cannot find such model type!");
            throw new NotFoundException("Cannot find such model type!", "Cannot find such model type!");
        }
        return modelType.get();
    }

    @Override
    public int[] getVersion(String modelTypeName) {
        Algorithm modelType = getModelType(modelTypeName);
        int[] result = new int[2];
        result[0] = modelType.getCurrentReleasedVersion();
        result[1] = modelType.getCurrentCachedVersion();
        return result;
    }

    @Override
    public void setVersion(String modelTypeName, int bigVersion, int smallVersion) {
        Algorithm modelType = getModelType(modelTypeName);

        modelType.setCurrentReleasedVersion(bigVersion);
        modelType.setCurrentCachedVersion(smallVersion);
        //TODO: persistent
    }

    @Override
    public void updateModelTypes(String modelTypeInfo) {
        List<Algorithm> modelTypes = persistence.getModelTypes();

        List<String> modelTypeStr = getModelTypes();
        Set<String> preModelTypeSet = new HashSet<>(modelTypeStr);

        Set<String> intersection = new HashSet<>();
        Set<String> newModelTypeSet = new HashSet<>();

        if (modelTypeInfo.contains(";")) {
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
            Iterator<Algorithm> iterator = modelTypes.iterator();
            while (iterator.hasNext()){
                Algorithm mt = iterator.next();
                if (!intersection.contains(mt.getAlgorithmName())) {
                    iterator.remove();
                }
            }
        }

        if (newModelTypeSet.size() > 0) {
            for (String mt : newModelTypeSet) {
                if (!mt.trim().equals("")) {
                    Algorithm modelType = new Algorithm();
                    modelType.setAlgorithmName(mt);
                    modelType.setCurrentReleasedVersion(-1);
                    modelType.setCurrentCachedVersion(-1);
                    modelTypes.add(modelType);
                }
            }
        }

        persistence.persistentModelTypes();
    }
}
