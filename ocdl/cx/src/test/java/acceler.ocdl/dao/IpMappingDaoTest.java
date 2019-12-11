package acceler.ocdl.dao;


import acceler.ocdl.entity.IpMapping;
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
public class IpMappingDaoTest {


    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IpMappingDao dao;

    private IpMapping test;

    @Before
    public void before() {

        test = IpMapping.builder()
                .name("test")
                .privateIp("55555")
                .publicIp("66666")
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

        IpMapping objInDb = dao.save(test);
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getPrivateIp(), test.getPrivateIp());
        assertEquals(objInDb.getPublicIp(), test.getPublicIp());

    }

    private void testRead() {
        // read
        IpMapping objInDb = dao.findByName(test.getName()).get();
        assertEquals(objInDb.getName(), test.getName());
        assertEquals(objInDb.getPrivateIp(), test.getPrivateIp());
        assertEquals(objInDb.getPublicIp(), test.getPublicIp());
    }

    private void testUpdate() {

        IpMapping objInDb = dao.findByName(test.getName()).get();
        String name = "data";
        Long id = objInDb.getId();
        objInDb.setName(name);
        objInDb = dao.save(objInDb);

        assertEquals(objInDb.getId(), id);
        assertEquals(objInDb.getName(), name);
        assertEquals(objInDb.getPrivateIp(), test.getPrivateIp());
        assertEquals(objInDb.getPublicIp(), test.getPublicIp());
    }

    private void testDelete() {

        IpMapping objInDb = dao.findByName(test.getName()).get();
        dao.delete(objInDb);
        Boolean exist = dao.findByName(test.getName()).isPresent();
        assertEquals(exist, false);
    }
}
