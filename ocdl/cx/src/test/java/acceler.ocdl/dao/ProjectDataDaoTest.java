package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.ProjectData;
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
public class ProjectDataDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProjectDataDao dao;

    private ProjectData test;

    @Autowired
    private ProjectDao projectDao;

    @Before
    public void before() {

        Project project = projectDao.findByName("ocdl").get();

        test = ProjectData.builder()
                .name("test")
                .refId("test")
                .suffix("tflite")
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

        ProjectData objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());

    }

    private void testRead() {
        // read
        ProjectData objInDb = dao.findByRefId(test.getRefId()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getRefId(), test.getRefId());
        assertEquals(objInDb.getSuffix(), test.getSuffix());
    }

    private void testUpdate() {

        ProjectData objInDb = dao.findByRefId(test.getRefId()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getSuffix(), test.getSuffix());
        assertEquals(objInDb.getRefId(), test.getRefId());
    }

    private void testDelete() {

        ProjectData objInDb = dao.findByRefId(test.getRefId()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByRefId(test.getRefId()).isPresent();
        assertEquals(exist, false);
    }

}