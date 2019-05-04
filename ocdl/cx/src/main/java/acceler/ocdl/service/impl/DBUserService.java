package acceler.ocdl.service.impl;

import acceler.ocdl.controller.AuthController;
import acceler.ocdl.exception.ExistedException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.model.InnerUser;
import acceler.ocdl.model.OauthUser;
import acceler.ocdl.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DBUserService implements UserService {

    @Override
    public boolean credentialCheck(AuthController.UserCredentials loginUser) {
        Optional<InnerUser> targetUserOpt = InnerUser.getUserByUserName(loginUser.account);
        return targetUserOpt.map(innerUser -> innerUser.getPassword().equals(loginUser.password)).orElse(false);
    }

    @Override
    public OauthUser getUserBySourceID(OauthUser.OauthSource source, String ID) throws NotFoundException {
        Optional<OauthUser> targetUserOpt = OauthUser.getUserBySourceID(source, ID);
        return targetUserOpt.orElseThrow(() -> new NotFoundException("User Not Found", "User Not Found"));
    }

    @Override
    public InnerUser getUserByUsernameAndPwd(String userName, String password) throws NotFoundException {
        return null;
    }

    @Override
    public OauthUser createUser(OauthUser.OauthSource source, String ID) throws ExistedException {
        if (OauthUser.existUser(source, ID)){
            throw new ExistedException();
        }
    }
}
