package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelDao extends CrudRepository<Model, Long> {

    List<Model> findByStatus(Model.Status status);

    List<Model> findByStatusAndProjectId(Model.Status status, Long projectId);

    List<Model> findByModelTypeIdAndProjectId(Long modelTypeId, Long ProjectId);

    List<Model> findByModelTypeIdAndProjectIdAndAndBigVersion(Long modelTypeId, Long ProjectId, Long bigVersion);

}
