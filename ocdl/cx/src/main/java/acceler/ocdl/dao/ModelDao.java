package acceler.ocdl.dao;

import acceler.ocdl.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ModelDao extends JpaRepository<Model, Long>, JpaSpecificationExecutor<Model> {

    Optional<Model> findByRefId(String refId);

}
