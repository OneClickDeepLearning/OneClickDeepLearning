package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.service.KubernetesService;
import acceler.ocdl.service.ModelService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultModelServiceTest {

    @Autowired
    private ModelService service;

    @Test
    public void copyModels() {
        User user1 = new User();
        user1.setProjectId(1L);
        user1.setUserId(3L);

        service.copyModels(user1);
        service.copyModels(user1);

    }
}