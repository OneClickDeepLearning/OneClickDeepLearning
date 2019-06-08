package acceler.ocdl.service.impl;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.exception.OcdlException;
import acceler.ocdl.model.AbstractUser;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.model.OauthUser;
import acceler.ocdl.model.Project;
import acceler.ocdl.service.HdfsService;
import acceler.ocdl.service.UserService;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import static org.apache.commons.io.FileUtils.forceMkdir;
import static org.apache.commons.io.FileUtils.forceMkdirParent;

@Service
@DependsOn({"storageLoader"})
public class DBUserService implements UserService {

    @Autowired
    HdfsService hdfsService;

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(loginUser.account);
        return targetUserOpt.map(innerUser -> innerUser.getPassword().equals(loginUser.password)).orElse(false);
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
    public OauthUser createUser(OauthUser.OauthSource source, String ID) throws ExistedException {
        if (OauthUser.existUser(source, ID)) {
            throw new ExistedException("Oauth User existed");
        }

        OauthUser newUser = OauthUser.createNewUser(source, ID);

        Path hadoopPath = new Path(CONSTANTS.HDFS.USER_SPACE + Project.getProjectNameInStorage() + newUser.getUserId());
        hdfsService.createDir(hadoopPath);

        return newUser;
    }

    @Override
    public InnerUser createUser(String userName, String password, AbstractUser.Role role) throws ExistedException {
        if (InnerUser.existUser(userName)){
            throw new ExistedException("inner user already existed");
        }

        InnerUser newUser = InnerUser.createNewUser(userName, password, role);
        Path hadoopPath = new Path(CONSTANTS.HDFS.USER_SPACE + newUser.getUserId());
        hdfsService.createDir(hadoopPath);

        try{
            File localMountSpace = new File(Paths.get(CONSTANTS.APPLICATIONS_DIR.USER_SPACE,
                    CONSTANTS.NAME_FORMAT.USER_SPACE.replace("{userId}", String.valueOf(newUser.getUserId()))).toString());
            forceMkdir(localMountSpace);
            System.out.println("==========================================");
            System.out.println("success create local directory");
        } catch (IOException e) {
            throw new OcdlException("Fail to creat mounted userspace for " + newUser.getUserName());
        }

        return newUser;
    }
}
