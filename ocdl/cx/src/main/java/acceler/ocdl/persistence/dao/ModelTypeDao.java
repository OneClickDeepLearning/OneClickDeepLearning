package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.ModelType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelTypeDao extends CrudRepository<ModelType, Long>{

}
