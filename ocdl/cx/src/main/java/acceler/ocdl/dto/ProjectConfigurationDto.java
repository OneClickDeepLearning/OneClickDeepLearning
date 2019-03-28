package acceler.ocdl.dto;

import acceler.ocdl.model.Project;

import java.io.Serializable;

public class ProjectConfigurationDto implements Serializable {
    private static final long serialVersionUID = 1L;


    private String projectName;
    private String k8Url;
    private String templatePath;
    private String gitPath;
    private String suffix;
    private String modelTypes;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getK8Url() {
        return k8Url;
    }

    public void setK8Url(String k8Url) {
        this.k8Url = k8Url;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getGitPath() { return gitPath; }

    public void setGitPath(String gitPath) { this.gitPath = gitPath; }

    public String getSuffix() { return suffix; }

    public void setSuffix(String suffix) { this.suffix = suffix; }

    public String getModelTypes() { return modelTypes; }

    public void setModelTypes(String modelTypes) {
        this.modelTypes = modelTypes;
    }

    public Project convert2Project() {

        Project project = new Project();
        project.setProjectName(this.projectName);
        project.setK8Url(this.k8Url);
        project.setTemplatePath(this.templatePath);
        project.setGitPath(this.gitPath);
        project.setSuffix(this.suffix);

        return project;
    }
}
