package acceler.ocdl.dao;

import acceler.ocdl.entity.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlgorithmDao extends JpaRepository<Algorithm, Long>, JpaSpecificationExecutor<Algorithm> {
}
