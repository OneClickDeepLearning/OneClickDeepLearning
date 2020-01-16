package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.dao.UserDao;
import acceler.ocdl.entity.User;
import acceler.ocdl.exception.InvalidParamException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.model.OauthUser;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.UserService;
import acceler.ocdl.utils.EncryptionUtil;
import acceler.ocdl.utils.TimeUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
//@DependsOn({"storageLoader"})
public class DBUserService implements UserService {

    @Autowired
    HdfsService hdfsService;

    @Autowired
    private UserDao userDao;

    @Value("${HDFS.USER_SPACE}")
    private String  hdfsUserSpace;
    @Value("${APPLICATIONS_DIR.USER_SPACE}")
    private String applicationsDirUserSpace;

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
//        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(loginUser.account);
        byte[] textBytes = Base64.decodeBase64(loginUser.password);
        String password = EncryptionUtil.decrypt(textBytes);
//        return targetUserOpt.map(innerUser -> innerUser.getPassword().equals(password)).orElse(false);
        return userDao.findByName(loginUser.account)
                .map(user -> user.getPassword().equals(password) )
                .orElseThrow(() ->
                        new NotFoundException(String.format("%s user not found", loginUser.account)));
    }

    @Override
    public OauthUser getUserBySourceID(OauthUser.OauthSource source, String ID) throws NotFoundException {
        Optional<OauthUser> targetUserOpt = OauthUser.getUserBySourceID(source, ID);
        return targetUserOpt.orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @Override
    public InnerUser getUserByUsername(String userName) throws NotFoundException {
        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(userName);
        return targetUserOpt.orElseThrow(() -> new NotFoundException("User Not Found"));
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
}
