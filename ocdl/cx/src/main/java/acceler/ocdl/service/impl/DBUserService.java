package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.dao.RUserRoleDao;
import acceler.ocdl.dao.RoleDao;
import acceler.ocdl.dao.UserDao;
import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.RUserRole;
import acceler.ocdl.entity.Role;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.InvalidParamException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.OauthUser;
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

import java.util.List;


@Service
//@DependsOn({"storageLoader"})
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
//        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(loginUser.account);
        /*byte[] textBytes = Base64.decodeBase64(loginUser.password);
        String password = EncryptionUtil.decrypt(textBytes);*/
//        return targetUserOpt.map(innerUser -> innerUser.getPassword().equals(password)).orElse(false);
        return userDao.findByUserName(loginUser.account)
                .map(user -> user.getPassword().equals(loginUser.password) )
                .orElseThrow(() ->
                        new NotFoundException(String.format("%s user not found", loginUser.account)));
    }

    @Override
    public User getUserBySourceID(OauthUser.OauthSource source, String id) {
//        Optional<OauthUser> targetUserOpt = OauthUser.getUserBySourceID(source, ID);
//        return targetUserOpt.orElseThrow(() -> new NotFoundException("User Not Found"));
        return userDao.findBySourceAndSourceId(source.name(), id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public User getUserByUsername(String userName) {
//        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(userName);
//        return targetUserOpt.orElseThrow(() -> new NotFoundException("User Not Found"));
        return userDao.findByUserName(userName)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public User getUserByUserId(Long id) throws NotFoundException {
        return userDao.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

//    @Override
//    public OauthUser createUser(OauthUser.OauthSource source, String ID) throws ExistedException {
//        if (OauthUser.existUser(source, ID)) {
//            throw new ExistedException("Oauth User existed");
//        }
//        OauthUser newUser = OauthUser.createNewUser(source, ID);
//
//        Path hadoopPath = new Path(hdfsUserSpace+ Project.getProjectNameInStorage() + newUser.getUserId());
//        hdfsService.createDir(hadoopPath);
//
//        return newUser;
//    }
//
//    @Override
//    public InnerUser createUser(String userName, String password, AbstractUser.Role role) throws ExistedException {
//        if (InnerUser.existUser(userName)){
//            throw new ExistedException("inner user already existed");
//        }
//
//        InnerUser newUser = InnerUser.createNewUser(userName, password, role);
//        Path hadoopPath = new Path(hdfsUserSpace + newUser.getUserId());
//        hdfsService.createDir(hadoopPath);
//
//        try{
//            File localMountSpace = new File(Paths.get(applicationsDirUserSpace,
//                    CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{userId}", String.valueOf(newUser.getUserId()))).toString());
//            forceMkdir(localMountSpace);
//        } catch (IOException e) {
//            throw new OcdlException("Fail to creat mounted userspace for " + newUser.getUserName());
//        }
//
//        return newUser;
//    }


    @Override
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

        boolean valid;
        if (user.getIsInnerUser()) {
            valid = !StringUtils.isEmpty(user.getUserName())
                    && !StringUtils.isEmpty(user.getPassword());
        } else {
            valid = !StringUtils.isEmpty(user.getUserName())
                    && !StringUtils.isEmpty(user.getSource())
                    && !StringUtils.isEmpty(user.getSourceId());
        }

        if (!valid) {
            throw new InvalidParamException("Incomplete user info.");
        }

        String current = TimeUtil.currentTimeStampStr();
        user.setCreatedAt(current);
        user.setUpdatedAt(current);
        user.setIsDeleted(false);
        return userDao.save(user);
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
    public RUserRole addRole(User user, Role role, Project project) {

        Role roleInDb = roleDao.findById(role.getId())
                .orElseThrow(() -> new NotFoundException("Fail to find role"));
        Project projectInDb = projectService.getProject(project.getId());
        User userInDb = getUserByUserId(user.getId());

        RUserRole rUserRole = RUserRole.builder()
                .user(userInDb)
                .role(roleInDb)
                .project(projectInDb)
                .build();

        return rUserRoleDao.save(rUserRole);

    }
}
