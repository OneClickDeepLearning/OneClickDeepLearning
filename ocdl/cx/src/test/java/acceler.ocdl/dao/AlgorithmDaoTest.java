package acceler.ocdl.dao;

import acceler.ocdl.entity.Algorithm;
import acceler.ocdl.entity.Project;
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
public class AlgorithmDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AlgorithmDao dao;

    @Autowired
    private ProjectDao projectDao;

    private Algorithm test;


    @Before
    public void before() {
        // init user
        Project project = projectDao.findByName("ocdl").get();

        // init project
        test = Algorithm.builder()
                .project(project)
                .name("NLP")
                .currentCachedVersion(0)
                .currentReleasedVersion(0)
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

        Algorithm objInDb = dao.save(test);

        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());

    }

    private void testRead() {
        // read
        Algorithm objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
    }

    private void testUpdate() {

        Algorithm objInDb = dao.findByName(test.getName()).get();
        String updateName = "KNN";
        Long id = objInDb.getId();
        objInDb.setName(updateName);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), updateName);
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
    }

    private void testDelete() {

        Algorithm objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }
}
