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


    public static void setProjectData(Project projectInfo) {
        lock.writeLock().lock();
        projectData.projectName = projectInfo.projectName;
        projectData.gitRepoURI = projectInfo.gitRepoURI;
        projectData.k8MasterUri = projectInfo.k8MasterUri;
        projectData.templatePath = projectInfo.templatePath;
        projectData.description = projectInfo.description;
        setSuffixesOfProject(projectInfo.suffixes);
        lock.writeLock().unlock();
    }

    public static void setSuffixesOfProject(List<String> newSuffixes) {
        lock.writeLock().lock();
        projectData.suffixes.clear();
        projectData.suffixes.addAll(newSuffixes);
        lock.writeLock().unlock();
    }

    public static String getProjectName() {
        lock.readLock().lock();
        String name = projectData.projectName;
        lock.readLock().unlock();

        return name;
    }

    public static String getGitRepoURI() {
        lock.readLock().lock();
        String gitURL = projectData.gitRepoURI;
        lock.readLock().unlock();

        return gitURL;
    }

    public static String getK8MasterUri() {
        lock.readLock().lock();
        String K8MasterURL = projectData.k8MasterUri;
        lock.readLock().unlock();

        return K8MasterURL;
    }

    public static String getTemplatePath() {
        lock.readLock().lock();
        String templatePath = projectData.templatePath;
        lock.readLock().unlock();

        return templatePath;
    }

    public static String getDescription() {
        lock.readLock().lock();
        String description = projectData.description;
        lock.readLock().unlock();

        return description;
    }

    public static String[] getModelFileSuffixes() {
        lock.readLock().lock();
        List<String> suffixes = projectData.suffixes;
        lock.readLock().unlock();
        return suffixes.toArray(new String[0]);
    }
}
