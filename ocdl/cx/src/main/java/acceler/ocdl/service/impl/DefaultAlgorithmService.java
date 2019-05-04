package acceler.ocdl.service.impl;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.service.AlgorithmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class DefaultAlgorithmService implements AlgorithmService {

    private static final Logger log = Logger.getLogger(DefaultAlgorithmService.class);

    @Override
    public List<Algorithm> getAllAlgorithm() {

        return Arrays.asList(Algorithm.getAlgorithms());
    }

    @Override
    public Algorithm getAlgorithmByName(String algorithmName) {
        return Algorithm.getAlgorithmByName(algorithmName).orElseThrow(() -> (new NotFoundException("Not found algorithm:" + algorithmName, "algorithm not found")));
    }

    @Override
    public List<String> updateAlgorithmList(List<String> algorithms, boolean forceRemove ) {

        // Add algorithm that is not exist in the current Algorithms
        algorithms.stream()
                .filter(algorithmName -> Algorithm.existAlgorithm(algorithmName))
                .forEach(algorithmName -> {
                    Algorithm addedAlgorithm = new Algorithm();
                    addedAlgorithm.setAlgorithmName(algorithmName);
                    Algorithm.addNewAlgorithm(addedAlgorithm);
                });

        // if force remove, then remove the models that not in the algorithms list but in the current algorithm
        if (forceRemove) {
            Algorithm[] currentAlgorithms = Algorithm.getAlgorithms();
            Arrays.stream(currentAlgorithms)
                    .filter(m -> algorithms.contains(m.getAlgorithmName()))
                    .forEach(m -> Algorithm.removeAlgorithm(m.getAlgorithmName()));
        }

        // Get updated algorithm name list
        List<String> updatedAlgorithmNames = new ArrayList<>();
        Arrays.stream(Algorithm.getAlgorithms()).forEach(m -> updatedAlgorithmNames.add(m.getAlgorithmName()));

        return updatedAlgorithmNames;
    }
}
