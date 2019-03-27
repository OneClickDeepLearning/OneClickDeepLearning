package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import acceler.ocdl.service.KubernetesService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class DefaultKubernetesServiceTest {

    @Autowired
    private KubernetesService service;

//    @Test
//    public void launchDockerContainer() {
//
//        User user1 = new User();
//        user1.setProjectId(1L);
//        user1.setUserId(1001L);
//        User user2 = new User();
//        user2.setProjectId(2L);
//        user2.setUserId(1002L);
//
//        String url = service.launchDockerContainer("cpu",user1);
//        System.out.println(url);
//        url = service.launchDockerContainer("gpu",user1);
//        System.out.println(url);
//
//        url = service.launchDockerContainer("cpu",user1);
//        System.out.println(url);
//        url = service.launchDockerContainer("gpu",user1);
//        System.out.println(url);
//
//        url = service.launchDockerContainer("cpu",user2);
//        System.out.println(url);
//        url = service.launchDockerContainer("gpu",user2);
//        System.out.println(url);
//    }
}