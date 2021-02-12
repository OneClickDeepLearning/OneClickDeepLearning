package acceler.ocdl.service;


import acceler.ocdl.entity.Algorithm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AlgorithmService {

    /**
     * create or update algorithm
     * @param algorithm the algorithm that received by front-end
     * @return updated algorithm
     */
    Algorithm saveAlgorithm(Algorithm algorithm);

    /**
     * get the algorithm list with condition
     * @return list of algorithm
     */
    Page<Algorithm> getAlgorithm(Algorithm algorithm, int page, int size);

    /**
     * delete algorithm list
     * @param algorithms the algorithm  list that aim to delete
     * @return boolean result to deletion
     */
    boolean batchDeleteAlgorithm(List<Algorithm> algorithms);



}
