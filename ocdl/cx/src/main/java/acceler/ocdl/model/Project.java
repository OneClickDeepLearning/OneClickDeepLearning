package acceler.ocdl.model;

import acceler.ocdl.dto.ProjectConfigurationDto;

import javax.persistence.*;

public class Project {
    private String projectName;
    private String gitPath;
    private String k8Url;
    private String templatePath;
    private String description;
    private String surffix;
    private String modelType;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSurffix() {
        return surffix;
    }

    public void setSurffix(String surffix) {
        this.surffix = surffix;
    }

    public String getModelType() {
        return modelType;
    }

    public void setModelType(String modelType) {
        this.modelType = modelType;
    }
}
