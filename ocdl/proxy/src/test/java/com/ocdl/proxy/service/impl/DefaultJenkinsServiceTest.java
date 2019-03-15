package com.ocdl.proxy.service.impl;

import com.ocdl.proxy.EndProxyApplication;
import com.offbytwo.jenkins.model.Job;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EndProxyApplication.class})
//@TestPropertySource(locations = "classpath:application-test.properties")
public class DefaultJenkinsServiceTest {

    @Autowired
    private DefaultJenkinsService jenkins;

    @Before
    public void setUp() throws Exception {
        jenkins.creatConn();
    }

    @Test
    public void testGetJob() {

        Map<String, Job> jobs = jenkins.getJobs();
        assertEquals(2, jobs.size());
    }

    @Test
    public void testGetJobXML() {

        String xml = jenkins.getJobXML("1");

    }

    @Test
    public void testCreateJob() {

        String projectName = "3";

        String desprition = "Project_" + projectName;
        String gitUrl = "http://ec2-54-89-140-122.compute-1.amazonaws.com/git/" + projectName;
        String topic = projectName + "_jkmsg";
        String fileName = topic + ".txt";


        String xml = jenkins.generateXML(desprition, gitUrl, topic, fileName);
        jenkins.createJob(projectName, xml);
        System.out.println("jenkins item created");
//        assertEquals(3, jenkins.getJobs().size());


    }

}