package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.Suffix;
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
public class SuffixDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SuffixDao dao;

    @Autowired
    private ProjectDao projectDao;

    private Suffix test;

    @Before
    public void before() {

        Project project = projectDao.findByName("ocdl").get();

        // init user data
        test = Suffix.builder()
                .name("tflite")
                .project(project)
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

        Suffix objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());

    }

    private void testRead() {
        // read
        Suffix objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
    }

    private void testUpdate() {

        Suffix objInDb = dao.findByName(test.getName()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
    }

    private void testDelete() {

        Suffix objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }

}
