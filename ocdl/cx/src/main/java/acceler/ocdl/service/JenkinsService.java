package acceler.ocdl.service;

public interface JenkinsService {

    void creatConn();

    String generateXML(String desp, String gitUrl, String topic, String outputFileName);

    void createJob(String jobName, String xml);
}
