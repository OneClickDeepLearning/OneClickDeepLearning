package acceler.ocdl.service;

import acceler.ocdl.model.Algorithm;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AlgorithmService {

    /**
     * get the algorithm list
     * @return list of algorithm
     */
    List<Algorithm> getAllAlgorithm();

    /**
     * get Algorithm obj by name
     * @param algorithmName the name of Algorithm
     * @return Algorithm obj
     */
    Algorithm getAlgorithmByName(String algorithmName);


    /**
     * update algorithm list
     * @param algorithms the algorithm list that received by front-end
     * @param forceRemove is force removed
     * @return list of updated algorithm name
     */
    List<String> updateAlgorithmList(List<String> algorithms);

}
