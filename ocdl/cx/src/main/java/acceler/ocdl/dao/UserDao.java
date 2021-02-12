package acceler.ocdl.dao;

import acceler.ocdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findBySourceId(String sourceId);

    Optional<User> findByUserName(String account);

    Optional<User> findBySourceAndSourceId(String source, String sourceId);

    List<User> findAllByUserNameContaining(String name);
}
