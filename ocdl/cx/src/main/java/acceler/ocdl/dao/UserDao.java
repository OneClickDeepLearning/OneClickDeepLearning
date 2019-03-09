package acceler.ocdl.dao;

import acceler.ocdl.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<Long, User>{
}
