package acceler.ocdl.service;

import acceler.ocdl.entity.User;
import acceler.ocdl.entity.UserData;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserDataService {


    @Transactional
    UserData uploadUserData(String srcPath, User user);

    Page<UserData> getUserData(UserData userData, int page, int size);

    boolean batchDeleteUserData(List<UserData> userDatas);

    boolean downloadUserData(String refId, User user);
}
