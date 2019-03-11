package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.ModelType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelTypeDao extends CrudRepository<ModelType, Long>{

    List<ModelType> findByProjectId(Long projectId);
//    ModelType findByName(String modelTypeName);

}
