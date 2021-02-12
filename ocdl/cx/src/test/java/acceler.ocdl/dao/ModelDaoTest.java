package acceler.ocdl.dao;

import acceler.ocdl.entity.*;
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
public class ModelDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ModelDao dao;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private AlgorithmDao algorithmDao;

    @Autowired
    private UserDao userDao;

    private Model test;


    @Before
    public void before() {

        Project project = projectDao.findByName("ocdl").get();

        Algorithm algorithm = algorithmDao.findByName("NLP").get();

        User user = userDao.findByEmail("ivyling063@gmail.com").get();

        test = Model.builder()
                .project(project)
                .name("model")
                .algorithm(algorithm)
                .project(project)
                .owner(user)
                .cachedVersion(0)
                .releasedVersion(0)
                .lastOperator(user)
                .refId("abcd")
                // 0 - new, 1 - approved, 2 - rejected
                .status(ModelStatus.NEW)
                .suffix("tflite")
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

        Model objInDb = dao.save(test);

        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getOwner().getId(), test.getOwner().getId());
        assertEquals(objInDb.getLastOperator().getId(), test.getLastOperator().getId());
        assertEquals(objInDb.getAlgorithm().getId(), test.getAlgorithm().getId());
        assertEquals(objInDb.getCachedVersion(), test.getCachedVersion());
        assertEquals(objInDb.getReleasedVersion(), test.getReleasedVersion());
        assertEquals(objInDb.getRefId(), test.getRefId());
    }

    private void testRead() {
        // read
        Model objInDb = dao.findByRefId(test.getRefId()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getOwner().getId(), test.getOwner().getId());
        assertEquals(objInDb.getLastOperator().getId(), test.getLastOperator().getId());
        assertEquals(objInDb.getAlgorithm().getId(), test.getAlgorithm().getId());
        assertEquals(objInDb.getCachedVersion(), test.getCachedVersion());
        assertEquals(objInDb.getReleasedVersion(), test.getReleasedVersion());
        assertEquals(objInDb.getRefId(), test.getRefId());
    }

    private void testUpdate() {

        Model objInDb = dao.findByRefId(test.getRefId()).get();
        String updateName = "model2222";
        Long id = objInDb.getId();
        objInDb.setName(updateName);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getName(), updateName);
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getOwner().getId(), test.getOwner().getId());
        assertEquals(objInDb.getLastOperator().getId(), test.getLastOperator().getId());
        assertEquals(objInDb.getAlgorithm().getId(), test.getAlgorithm().getId());
        assertEquals(objInDb.getCachedVersion(), test.getCachedVersion());
        assertEquals(objInDb.getReleasedVersion(), test.getReleasedVersion());
        assertEquals(objInDb.getRefId(), test.getRefId());
    }

    private void testDelete() {

        Model objInDb = dao.findByRefId(test.getRefId()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByRefId(test.getRefId()).isPresent();
        assertEquals(exist, false);
    }
}
