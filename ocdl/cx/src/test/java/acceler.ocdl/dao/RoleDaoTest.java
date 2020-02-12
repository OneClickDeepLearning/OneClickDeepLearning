package acceler.ocdl.dao;

import acceler.ocdl.entity.Role;
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
public class RoleDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleDao dao;

    private Role test;

    @Autowired
    private UserDao userDao;

    @Before
    public void before() {

        User user = userDao.findByEmail("ivyling063@gmail.com").get();

        test = Role.builder()
                .name("admin")
                .description("admin")
                .build();

    }

    @Test
    public void testCRUD() {
        testCreate();
        testRead();
        testUpdate();
        testDelete();
    }

    private void testCreate() {

        Role objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());

    }

    private void testRead() {
        // read
        Role objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());

    }

    private void testUpdate() {

        Role objInDb = dao.findByName(test.getName()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getDescription(), test.getDescription());

    }

    private void testDelete() {

        Role objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }
}
