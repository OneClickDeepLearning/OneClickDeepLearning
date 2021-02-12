package acceler.ocdl.dao;

import acceler.ocdl.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface UserDataDao extends JpaRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {

    Optional<UserData> findByRefId(String refId);

    List<UserData> findByNameAndIsDeletedIsFalse(String name);

    Optional<UserData> findByIdAndIsDeletedIsFalse(Long id);
}
