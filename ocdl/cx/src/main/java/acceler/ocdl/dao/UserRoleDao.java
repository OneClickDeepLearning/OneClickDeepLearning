package acceler.ocdl.dao;

import acceler.ocdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;



public interface UserRoleDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUserName(String account);

    Optional<User> findBySourceAndSourceId(String source, String sourceId);
}
