package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.Model;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelDao extends CrudRepository<Model, Long> {

}
