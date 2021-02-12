package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.controller.AuthController;
import acceler.ocdl.dao.RUserRoleDao;
import acceler.ocdl.dao.RoleDao;
import acceler.ocdl.dao.UserDao;
import acceler.ocdl.entity.*;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.ProjectService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.EncryptionUtil;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import static org.apache.tomcat.util.http.fileupload.FileUtils.forceMkdir;


@Service
public class DBUserService implements UserService {

    @Autowired
    HdfsService hdfsService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private RUserRoleDao rUserRoleDao;

    @Value("${HDFS.USER_SPACE}")
    private String  hdfsUserSpace;

    @Value("${APPLICATIONS_DIR.USER_SPACE}")
    private String applicationsDirUserSpace;

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        return userDao.findByUserName(loginUser.account)
                .map(user -> user.getPassword().equals(loginUser.password) )
                .orElseThrow(() ->
                        new NotFoundException(String.format("%s user not found", loginUser.account)));
    }

    @Override
    public User getUserBySourceID(OauthSource source, String id) {
        return userDao.findBySourceAndSourceId(source.name(), id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public User getUserByUsername(String userName) {
        return userDao.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public User getUserByUserId(Long id) throws NotFoundException {
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    @Transactional
    public User saveUser(User user) {
        User userInDb = null;
        if (user.getId() != null) {
            userInDb = updateUser(user);
        }else {
            userInDb = createUser(user);
        }
        return userInDb;
    }

    private User updateUser(User user) {
        
        User userInDb = userDao.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("%s user isn't found.", user.getId())));
        
        if (!StringUtils.isEmpty(user.getUserName())) {
            userInDb.setUserName(user.getUserName());
        }

        if (!StringUtils.isEmpty(user.getEmail())) {
            userInDb.setEmail(user.getEmail());
        }
        
        if (!StringUtils.isEmpty(user.getPassword())) {
            byte[] textBytes = Base64.decodeBase64(user.getPassword());
            String password = EncryptionUtil.decrypt(textBytes);
            userInDb.setPassword(password);
        }
        if (user.getIsDeleted() != null) {
            if (user.getIsDeleted() == true) {
                userInDb.setDeletedAt(TimeUtil.currentTimeStampStr());
            }
            userInDb.setIsDeleted(user.getIsDeleted());
        }

        return userDao.save(userInDb);
        
    }

    private User createUser(User user) {

        if (user.getUserName() != null && userDao.findByUserName(user.getUserName()).isPresent()) {
            throw new OcdlException("Users name already exist.");
        }

        if (user.getEmail() != null && userDao.findByEmail(user.getEmail()).isPresent()) {
            throw new OcdlException("Email already used.");
        }

        String current = TimeUtil.currentTimeStampStr();
        user.setCreatedAt(current);
        user.setUpdatedAt(current);
        user.setIsDeleted(false);
        User newUser = userDao.save(user);

        //Path hadoopPath = new Path(hdfsUserSpace + newUser.getId());
        //hdfsService.createDir(hadoopPath);

        try{
            File localMountSpace = new File(Paths.get(applicationsDirUserSpace,
                    CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{userId}", String.valueOf(newUser.getId()))).toString());
            forceMkdir(localMountSpace);
        } catch (IOException e) {
            throw new OcdlException("Fail to creat mounted userspace for " + newUser.getUserName());
        }

        return newUser;

    }


    @Override
    public List<User> getAllUserByNameContaining(String name) {
        return userDao.findAllByUserNameContaining(name);
    }

    @Override
    public List<Role> getAllRole() {
        return roleDao.findAllByIsDeletedIsFalse();
    }

    @Override
    @Transactional
    public RUserRole addRoleRelation(User user, Role role, Project project) {

        Role roleInDb = roleDao.findById(role.getId())
                .orElseThrow(() -> new NotFoundException("Fail to find role"));
        Project projectInDb = projectService.getProject(project.getId());
        User userInDb = getUserByUserId(user.getId());

        RUserRole rUserRole = RUserRole.builder()
                .user(userInDb)
                .role(roleInDb)
                .project(project)
                .createdAt(TimeUtil.currentTimeStampStr())
                .isDeleted(false)
                .build();

        return rUserRoleDao.save(rUserRole);

    }

    @Override
    @Transactional
    public RUserRole deleteRoleRelation(Long id) {

        RUserRole rUserRole = rUserRoleDao.findById(id)
                .orElseThrow(()-> new OcdlException("Fail to find RUserRole."));

        rUserRole.setIsDeleted(true);
        rUserRole.setDeletedAt(TimeUtil.currentTimeStampStr());
        return rUserRoleDao.save(rUserRole);

    }

    @Override
    public boolean isExist(String sourceId) {
        return userDao.findBySourceId(sourceId).isPresent();
    }

    @Override
    public List<RUserRole> getProjectsByUser(User user) {
        return rUserRoleDao.findAllByUserAndIsDeletedIsFalse(user);
    }
}
