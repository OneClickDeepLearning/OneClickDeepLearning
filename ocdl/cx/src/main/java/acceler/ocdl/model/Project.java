package acceler.ocdl.model;

import acceler.ocdl.dto.ProjectConfigurationDto;
import java.io.Serializable;

public class Project implements Serializable {

    private String projectName;
    private String gitPath;
    private String k8Url;
    private String templatePath;
    private String description;
    private String suffix;

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

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public ProjectConfigurationDto convert2ProjectDto() {

        ProjectConfigurationDto projectConfigurationDto = new ProjectConfigurationDto();
        projectConfigurationDto.setProjectName(this.getProjectName());
        projectConfigurationDto.setK8Url(this.getK8Url());
        projectConfigurationDto.setGitPath(this.getGitPath());
        projectConfigurationDto.setTemplatePath(this.getTemplatePath());
        projectConfigurationDto.setSuffix(this.getSuffix());

        return projectConfigurationDto;
    }
}
