package acceler.ocdl.service.impl;

import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.Algorithm;
import acceler.ocdl.service.AlgorithmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<String> updateAlgorithmList(List<String> algorithms, boolean forceRemove) {
        return null;
    }
}
