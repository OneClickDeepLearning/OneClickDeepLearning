package acceler.ocdl.dao;

import acceler.ocdl.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDataDao extends JpaRepository<UserData, Long>, JpaSpecificationExecutor<UserData> {
}
