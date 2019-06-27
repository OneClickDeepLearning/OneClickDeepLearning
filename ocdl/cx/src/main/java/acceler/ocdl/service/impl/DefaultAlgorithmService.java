package acceler.ocdl.service.impl;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.service.AlgorithmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@DependsOn({"storageLoader"})
public class DefaultAlgorithmService implements AlgorithmService {

    private static final Logger log = Logger.getLogger(DefaultAlgorithmService.class);

    @Override
    public List<Algorithm> getAllAlgorithm() {

        return Arrays.asList(Algorithm.getAlgorithms());
    }

    @Override
    public Algorithm getAlgorithmByName(String algorithmName) {
        return Algorithm.getAlgorithmByName(algorithmName).orElseThrow(() -> (new NotFoundException("Not found algorithm:" + algorithmName)));
    }

    @Override
    public List<String> updateAlgorithmList(List<String> algorithms, boolean forceRemove) {
        Algorithm[] existedAlgorithms = Algorithm.getAlgorithms();
        List<String> overlap = new ArrayList<>();

        for (Algorithm algorithm : existedAlgorithms) {
            if (algorithms.contains(algorithm.getAlgorithmName())) {
                overlap.add(algorithm.getAlgorithmName());
            }
        }

        if (forceRemove) {
            //only need to add new no-existed algorithms
            for (Algorithm algorithm : existedAlgorithms) {
                if (!overlap.contains(algorithm.getAlgorithmName())) {
                    Algorithm.removeAlgorithm(algorithm.getAlgorithmName());
                }
            }
        }

        //new algorithms always need to append
        for (String s : algorithms) {
            if (!overlap.contains(s)) {
                Algorithm newAlgorithm = new Algorithm();
                newAlgorithm.setAlgorithmName(s);
                Algorithm.addNewAlgorithm(newAlgorithm);
            }
        }

        // get updated algorithm name list
        List<String> updatedAlgorithmNames = new ArrayList<>();
        Arrays.stream(Algorithm.getAlgorithms()).forEach(m -> updatedAlgorithmNames.add(m.getAlgorithmName()));

        return updatedAlgorithmNames;
    }
}
