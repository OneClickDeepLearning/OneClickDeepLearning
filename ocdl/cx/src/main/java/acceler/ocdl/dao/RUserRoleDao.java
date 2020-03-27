package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.RUserRole;
import acceler.ocdl.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RUserRoleDao extends JpaRepository<RUserRole, Long> {

    List<RUserRole> findAllByUserAndProject(User user, Project project);
}
