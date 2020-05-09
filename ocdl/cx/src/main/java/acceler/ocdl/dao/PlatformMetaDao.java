package acceler.ocdl.dao;

import acceler.ocdl.entity.PlatformMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface PlatformMetaDao extends JpaRepository<PlatformMeta, Long> {

    Optional<PlatformMeta> findByHadoopUrl(String url);
}
