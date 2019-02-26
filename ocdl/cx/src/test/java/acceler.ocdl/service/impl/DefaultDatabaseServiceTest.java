package acceler.ocdl.service.impl;

import acceler.ocdl.OcdlApplication;
import acceler.ocdl.model.User;
import acceler.ocdl.service.impl.DefaultDatabaseService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OcdlApplication.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class DefaultDatabaseServiceTest {

    @Autowired
    private DefaultDatabaseService db;

    private User user;


    @Before
    public void setUp() throws Exception {
        db.createConn();
    }

    @Test
    public void insertUser() {
        user = new User("test", "account", "1234", 1, User.Role.TEST);
        int id = db.insertUser(user);
        System.out.println("the user id = " + id );
        assertTrue(id>0);
    }

    @Test
    public void createNewRole() {
        User.Role role = User.Role.TEST;
        int id = db.createNewRole(role);
        System.out.println("the new role id = " + id );
        assertTrue(id>0);

    }

    @Test
    public void getRoleId() {
        User.Role role = User.Role.MANAGER;
        int id = db.getRoleId(role);
        // note increment id will be different
        assertEquals(2, id);
    }


    @Configuration
    @ComponentScan(basePackages = {"acceler.ocdl.service"})
    public class Testconfig{

    }
}