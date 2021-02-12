package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.Suffix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface SuffixDao extends JpaRepository<Suffix, Long>, JpaSpecificationExecutor<Suffix> {

    Optional<Suffix> findByName(String name);

    Optional<Suffix> findByIdAndIsDeletedIsFalse(Long id);

    List<Suffix> findAllByProject(Project project);
}
