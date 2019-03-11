package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDao extends CrudRepository<Model, Long> {

    List<Model> findByStatus(Model.Status status);

    List<Model> findByModelTypeIdAndProjectId(Long modelTypeId, Long ProjectId);

}
