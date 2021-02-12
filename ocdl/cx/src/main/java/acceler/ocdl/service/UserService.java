package acceler.ocdl.service;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.entity.*;
import acceler.ocdl.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {

    boolean credentialCheck(AuthController.UserCredentials loginUser);

    User getUserByUsername(String userName) throws NotFoundException;

    User getUserBySourceID(OauthSource source, String ID) throws NotFoundException;

    User getUserByUserId(Long id) throws NotFoundException;

    User saveUser(User user);

    List<User> getAllUserByNameContaining(String name);

    List<Role> getAllRole();

    @Transactional
    RUserRole addRoleRelation(User user, Role role, Project project);

    @Transactional
    RUserRole deleteRoleRelation(Long id);

    boolean isExist(String sourceId);

    List<RUserRole> getProjectsByUser(User user);
}
