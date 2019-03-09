package acceler.ocdl.model;

import acceler.ocdl.dto.ProjectConfigurationDto;

import javax.persistence.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long projectId;

    @Column(name = "name")
    private String projectName;

    @Column(name = "git_path")
    private String gitPath;

    @Column(name = "k8_url")
    private String k8Url;

    @Column(name = "template_path")
    private String templatePath;

    @Column(name = "description")
    private String description;

    public Project() {}

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

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

//    public ProjectConfigurationDto transfer2ProjectDto(){
//        ProjectConfigurationDto p = new ProjectConfigurationDto();
//        p.setProjectName(this.projectName);
//        p.setGitUrl(this.gitPath);
//        p.setK8Url(this.k8Url);
//        p.setTemplatePath(this.templatePath);
//
//        return p;
//    }

}
