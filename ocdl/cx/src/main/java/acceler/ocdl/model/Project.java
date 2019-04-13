package acceler.ocdl.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Project implements Serializable {
    private static final long serialVersionUID = -2767605614048989439L;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Project projectData = new Project();

    private String projectName;

    private String gitRepoURI;

    private String k8MasterUri;

    private String templatePath;

    private String description;

    private List<String> suffixes;

    private Project() {
        this.suffixes = new ArrayList<>();
    }

    public Project deepCopy() {
        Project copy = new Project();
        copy.projectName = this.projectName;
        copy.gitRepoURI = this.gitRepoURI;
        copy.k8MasterUri = this.k8MasterUri;
        copy.templatePath = this.templatePath;
        copy.description = this.description;
        copy.suffixes = new ArrayList<>();
        copy.suffixes.addAll(this.suffixes);

        return copy;
    }

    public Project getProjectInfo() {
        lock.readLock().lock();
        Project copy = projectData.deepCopy();
        lock.readLock().unlock();

        return copy;
    }

    public void setProjectData(Project projectInfo) {
        lock.writeLock().lock();
        projectData.projectName = projectInfo.projectName;
        projectData.gitRepoURI = projectInfo.gitRepoURI;
        projectData.k8MasterUri = projectInfo.k8MasterUri;
        projectData.templatePath = projectInfo.templatePath;
        projectData.description = projectInfo.description;
        setSuffixesOfProject(projectInfo.suffixes);
        lock.writeLock().unlock();
    }

    public void setSuffixesOfProject(List<String> newSuffixes) {
        lock.writeLock().lock();
        projectData.suffixes.clear();
        projectData.suffixes.addAll(newSuffixes);
        lock.writeLock().unlock();
    }

    public String getProjectName() {
        return this.projectName;
    }

    public String getGitRepoURI() {
        return this.gitRepoURI;
    }

    public String getK8MasterUri() {
        return this.k8MasterUri;
    }

    public String getTemplatePath() {
        return this.templatePath;
    }

    public String getDescription() {
        return this.description;
    }
}
