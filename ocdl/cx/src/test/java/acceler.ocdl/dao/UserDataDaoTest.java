package acceler.ocdl.dao;

import acceler.ocdl.entity.User;
import acceler.ocdl.entity.UserData;
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
public class UserDataDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserDataDao userDataDao;

    private UserData test;

    @Before
    public void before() {

        /// init user
        User user = User.builder()
                .userName("Ann")
                .email("ann@gmail.com")
                .password("123456")
                //.type("ocdl")
                .build();


        // init user data
        test = UserData.builder()
                .name("test")
                .refId("test")
                .suffix("tflite")
                .user(user)
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

        UserData objInDb = userDataDao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());

    }

    private void testRead() {
        // read
        UserData objInDb = userDataDao.findByRefId(test.getRefId()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
    }

    private void testUpdate() {

        UserData objInDb = userDataDao.findByRefId(test.getRefId()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = userDataDao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getSuffix(), test.getSuffix());
        assertEquals(objInDb.getRefId(), test.getRefId());
    }

    private void testDelete() {

        UserData objInDb = userDataDao.findByRefId(test.getRefId()).get();
        userDataDao.delete(objInDb);
        Boolean exist = userDataDao.findByRefId(test.getRefId()).isPresent();
        assertEquals(exist, false);
    }

}