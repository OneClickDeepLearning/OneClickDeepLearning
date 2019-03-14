package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultModelServiceTest {

    @Test
    public void copyModels() {

        User user1 = new User();
        user1.setProjectId(1L);
        user1.setUserId(1001L);
        User user2 = new User();
        user2.setProjectId(2L);
        user2.setUserId(1002L);

        DefaultModelService service = new DefaultModelService();

        service.copyModels(user1);
        service.copyModels(user1);

    }
}