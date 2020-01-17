package acceler.ocdl.dao;

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

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class UserDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDao userDao;

    private User test;

    @Before
    public void before() {
        // init user
        test = User.builder()
                .userName("Ann")
                .email("ann@gmail.com")
                .password("123456")
                //.type("ocdl")
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

        User objInDb = userDao.save(test);
        assertEquals(objInDb.getUserName(), test.getUserName());
        assertEquals(objInDb.getEmail(), test.getEmail());
        assertEquals(objInDb.getPassword(), test.getPassword());

    }

    private void testRead() {
        // read
        User objInDb = userDao.findByEmail(test.getEmail()).get();
        assertEquals(objInDb.getUserName(), test.getUserName());
        assertEquals(objInDb.getEmail(), test.getEmail());
        assertEquals(objInDb.getPassword(), test.getPassword());
    }

    private void testUpdate() {

        User objInDb = userDao.findByEmail(test.getEmail()).get();
        String updateName = "ivy";
        Long id = objInDb.getId();
        objInDb.setUserName(updateName);
        objInDb = userDao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getUserName(), updateName);
        assertEquals(objInDb.getEmail(), test.getEmail());
        assertEquals(objInDb.getPassword(), test.getPassword());
    }

    private void testDelete() {

        User objInDb = userDao.findByEmail(test.getEmail()).get();
        userDao.delete(objInDb);
        Boolean exist = userDao.findByEmail(test.getEmail()).isPresent();
        assertEquals(exist, false);
    }

}