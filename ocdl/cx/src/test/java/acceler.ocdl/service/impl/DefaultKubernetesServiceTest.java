package acceler.ocdl.service.impl;

import acceler.ocdl.service.KubernetesService;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultKubernetesServiceTest {

    @Autowired
    private KubernetesService service;

//    @Test
//    public void launchDockerContainer() {
//
//        InnerUser user1 = new InnerUser();
//        user1.setProjectId(1L);
//        user1.setUserId(1001L);
//        InnerUser user2 = new InnerUser();
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