package acceler.ocdl.persistence.dao;

import acceler.ocdl.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDao extends CrudRepository<User, Long>{

    List<User> findByAccountAndPassword(String account, String password);
}
