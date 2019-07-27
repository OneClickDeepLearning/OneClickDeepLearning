//package acceler.ocdl.service.impl;
//
//import acceler.ocdl.OcdlApplication;
//import com.offbytwo.jenkins.model.Job;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {OcdlApplication.class})
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class JenkinsServiceTest {
//
//    @Autowired
//    private DefaultJenkinsService jenkins;
//
//    @Before
//    public void setUp() throws Exception {
//        jenkins.creatConn();
//    }
//
//    @Test
//    public void testGetJob() {
//
//        Map<String, Job> jobs = jenkins.getJobs();
//        assertEquals(2, jobs.size());
//    }
//
//    @Test
//    public void testGetJobXML() {
//
//        String xml = jenkins.getJobXML("OneClickDeepLearning");
//
//    }
//
//    @Test
//    public void testCreateJob() {
//
//        String xml = jenkins.generateXML("company", "http://hello.com", "company_jkmsg", "conmpay_jkmsg.txt");
//        jenkins.createJob("test", xml);
//        assertEquals(3, jenkins.getJobs().size());
//
//    }
//
//}