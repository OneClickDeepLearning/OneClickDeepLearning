package acceler.ocdl.service.impl;

import acceler.ocdl.service.JenkinsService;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Job;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DefaultJenkinsService implements JenkinsService {

    // todo: modify the accesskey and secretkey
    private String url;
    private String userName;
    private String password;
    private String configXml;

    private JenkinsServer jenkins;

    public DefaultJenkinsService() {
        configXml = "<?xml version='1.1' encoding='UTF-8'?>\n" +
                "<project>\n" +
                "  <actions/>\n" +
                "  <description>###description###</description>\n" +
                "  <keepDependencies>false</keepDependencies>\n" +
                "  <properties/>\n" +
                "  <scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.9.3\">\n" +
                "    <configVersion>2</configVersion>\n" +
                "    <userRemoteConfigs>\n" +
                "      <hudson.plugins.git.UserRemoteConfig>\n" +
                "        <url>###git_url###</url>\n" +
                "      </hudson.plugins.git.UserRemoteConfig>\n" +
                "    </userRemoteConfigs>\n" +
                "    <branches>\n" +
                "      <hudson.plugins.git.BranchSpec>\n" +
                "        <name>*/master</name>\n" +
                "      </hudson.plugins.git.BranchSpec>\n" +
                "    </branches>\n" +
                "    <doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\n" +
                "    <submoduleCfg class=\"list\"/>\n" +
                "    <extensions/>\n" +
                "  </scm>\n" +
                "  <canRoam>true</canRoam>\n" +
                "  <disabled>false</disabled>\n" +
                "  <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\n" +
                "  <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\n" +
                "  <authToken>admin</authToken>\n" +
                "  <triggers/>\n" +
                "  <concurrentBuild>true</concurrentBuild>\n" +
                "  <builders>\n" +
                "    <com.cloudbees.jenkins.GitHubSetCommitStatusBuilder plugin=\"github@1.29.3\">\n" +
                "      <statusMessage>\n" +
                "        <content></content>\n" +
                "      </statusMessage>\n" +
                "      <contextSource class=\"org.jenkinsci.plugins.github.status.sources.DefaultCommitContextSource\"/>\n" +
                "    </com.cloudbees.jenkins.GitHubSetCommitStatusBuilder>\n" +
                "    <hudson.tasks.Shell>\n" +
                "      <command>#!/bin/bash\n" +
                "pwd\n" +
                "whoami\n" +
                "echo &quot;trying go to /var/lib/kafka_2.11-2.1.0&quot;\n" +
                "cd /var/lib/kafka_2.11-2.1.0\n" +
                "pwd\n" +
                "# bin/kafka-console-producer.sh --broker-list localhost:9092 --topic jkmsg\n" +
                "echo &apos;###topic###&apos; &gt;&gt; ###kafka_output_file_name###\n" +
                " \n" +
                "echo &quot;==========&quot;</command>\n" +
                "    </hudson.tasks.Shell>\n" +
                "  </builders>\n" +
                "  <publishers/>\n" +
                "  <buildWrappers/>\n" +
                "</project>";
    }

    @Value("http://54.89.140.122:8081")
    public void setUrl(String url) { this.url = url; }

    @Value("admin")
    public void setUserName(String userName) { this.userName = userName; }

    @Value("abcd1234")
    public void setPassword(String password) { this.password = password; }

    @Override
    public void creatConn() {

        System.out.println(url);
        System.out.println(userName);
        System.out.println(password);

        try {
            if (jenkins == null){
                jenkins = new JenkinsServer(new URI(url), userName, password);
            }
        } catch (URISyntaxException e ) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, Job> getJobs() {
        Map<String, Job> jobs = new HashMap<String, Job>();
        try {
            jobs = jenkins.getJobs();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return jobs;
    }

    public String getJobXML(String jobName) {

        String result = "";
        try{
            result = jenkins.getJobXml(jobName);
            System.out.println("=====================================================================");
            System.out.println(result);
            System.out.println("=====================================================================");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    @Override
    public String generateXML(String desp, String gitUrl, String topic, String outputFileName) {

        String result = configXml.replace("###description###", desp); // projectName
        result = result.replace("###git_url###", gitUrl);
        result = result.replace("###topic###", topic);
        result = result.replace("###kafka_output_file_name###", outputFileName);

        return result;
    }

    @Override
    public void createJob(String jobName, String xml) {

        try{
            jenkins.createJob(jobName, xml, true);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }









}
