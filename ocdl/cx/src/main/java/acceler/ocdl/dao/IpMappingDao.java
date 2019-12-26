package acceler.ocdl.dao;


import acceler.ocdl.entity.IpMapping;
import acceler.ocdl.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpMappingDao extends JpaRepository<IpMapping, Long> {

    Optional<IpMapping> findByName(String name);

}
