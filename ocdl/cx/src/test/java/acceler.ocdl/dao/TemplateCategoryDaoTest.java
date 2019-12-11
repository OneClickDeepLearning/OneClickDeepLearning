package acceler.ocdl.dao;

import acceler.ocdl.entity.Project;
import acceler.ocdl.entity.TemplateCategory;
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
public class TemplateCategoryDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TemplateCategoryDao dao;

    @Autowired
    private ProjectDao projectDao;

    private TemplateCategory test;

    @Before
    public void before() {

        Project project = projectDao.findByName("ocdl").get();

        // init user data
        test = TemplateCategory.builder()
                .name("common")
                .description("common")
                .project(project)
                .shared(false)
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

        TemplateCategory objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getShared(), test.getShared());

    }

    private void testRead() {
        // read
        TemplateCategory objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getShared(), test.getShared());
    }

    private void testUpdate() {

        TemplateCategory objInDb = dao.findByName(test.getName()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getDescription(), test.getDescription());
        assertEquals(objInDb.getProject().getId(), test.getProject().getId());
        assertEquals(objInDb.getShared(), test.getShared());
    }

    private void testDelete() {

        TemplateCategory objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }
}
