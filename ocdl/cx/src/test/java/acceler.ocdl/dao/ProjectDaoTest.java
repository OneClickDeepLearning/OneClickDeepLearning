package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class ProjectDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectDao dao;

    @Autowired
    private UserDao userDao;

    private Project test;

    private User user;

    @Before
    public void before() {
        // init user
        User user = userDao.findByEmail("ivyling063@gmail.com").get();

        // init project
        test = Project.builder()
                .name("VL")
                .description("code vulnerability")
                .build();

        //test.setUserList(new ArrayList<>());
        //test.getUserList().add(user);

    }

    @Test
    public void testCRUD() {
        testCreate();
        testRead();
        testUpdate();
        testDelete();
    }

    private void testCreate() {

        Project objInDb = dao.saveAndFlush(test);

        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        //assertEquals(objInDb.getUserList().size(), test.getUserList().size());

    }

    private void testRead() {
        // read
        Project objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        //assertEquals(objInDb.getUserList().size(), test.getUserList().size());
    }

    private void testUpdate() {

        Project objInDb = dao.findByName(test.getName()).get();
        String updateName = "ocdl";
        Long id = objInDb.getId();
        objInDb.setName(updateName);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), updateName);
        assertEquals(objInDb.getDescription(), test.getDescription());
        //assertEquals(objInDb.getUserList().size(), test.getUserList().size());
    }

    private void testDelete() {

        Project objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }
}
