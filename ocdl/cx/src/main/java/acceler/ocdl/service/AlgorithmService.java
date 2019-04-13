package acceler.ocdl.service;

import acceler.ocdl.model.Algorithm;

import java.util.List;

public interface AlgorithmService {

    List<Algorithm> getAllAlgorithm(boolean containActive);

    Algorithm getAlgorithmByName(String algorithmName);


    /**
     * update algorithm list
     * @param algorithms
     * @param forceRemove force removed
     * @return list of updated algorithm name
     */
    List<String> updateAlgorithmList(List<String> algorithms, boolean forceRemove);

}
