package acceler.ocdl.dao;

import acceler.ocdl.entity.PlatformMeta;
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
public class PlatformMetaDaoTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PlatformMetaDao dao;

    private PlatformMeta test;

    @Before
    public void before() {

        test = PlatformMeta.builder()
                .k8Url("url")
                .kafkaUrl("url")
                .hadoopUrl("url")
                .modelUrl("url")
                .templateUrl("url")
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

        PlatformMeta objInDb = dao.save(test);
        assertEquals(objInDb.getHadoopUrl(), test.getHadoopUrl());
        assertEquals(objInDb.getK8Url(), test.getK8Url());
        assertEquals(objInDb.getKafkaUrl(), test.getKafkaUrl());
        assertEquals(objInDb.getModelUrl(),test.getModelUrl());
        assertEquals(objInDb.getTemplateUrl(), test.getTemplateUrl());
    }

    private void testRead() {
        // read
        PlatformMeta objInDb = dao.findByHadoopUrl(test.getHadoopUrl()).get();
        assertEquals(objInDb.getHadoopUrl(), test.getHadoopUrl());
        assertEquals(objInDb.getK8Url(), test.getK8Url());
        assertEquals(objInDb.getKafkaUrl(), test.getKafkaUrl());
        assertEquals(objInDb.getModelUrl(),test.getModelUrl());
        assertEquals(objInDb.getTemplateUrl(), test.getTemplateUrl());
    }

    private void testUpdate() {

        PlatformMeta objInDb = dao.findByHadoopUrl(test.getHadoopUrl()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setK8Url(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getHadoopUrl(), test.getHadoopUrl());
        assertEquals(objInDb.getK8Url(), test.getK8Url());
        assertEquals(objInDb.getKafkaUrl(), test.getKafkaUrl());
        assertEquals(objInDb.getModelUrl(),test.getModelUrl());
        assertEquals(objInDb.getTemplateUrl(), test.getTemplateUrl());
    }

    private void testDelete() {

        PlatformMeta objInDb = dao.findByHadoopUrl(test.getHadoopUrl()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByHadoopUrl(test.getHadoopUrl()).isPresent();
        assertEquals(exist, false);
    }
}
