package acceler.ocdl.model;

import acceler.ocdl.dto.ProjectConfigurationDto;

public class Project {

    private int projectId;
    private String projectName;
    private String git;
    private String k8Url;
    private String templateUrl;
    private String desp;


    public Project() {}

    public Project(String projectName, String git, String k8Url, String templateUrl, String desp) {
        this.projectName = projectName;
        this.git = git;
        this.k8Url = k8Url;
        this.templateUrl = templateUrl;
        this.desp = desp;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getK8Url() {
        return k8Url;
    }

    public void setK8Url(String k8Url) {
        this.k8Url = k8Url;
    }

    public String getTemplateUrl() {
        return templateUrl;
    }

    public void setTemplateUrl(String templateUrl) {
        this.templateUrl = templateUrl;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public ProjectConfigurationDto transfer2ProjectDto(){
        ProjectConfigurationDto p = new ProjectConfigurationDto();
        p.setProjectName(this.projectName);
        p.setGitUrl(this.git);
        p.setK8Url(this.k8Url);
        p.setTemplatePath(this.templateUrl);

        return p;
    }

}
