package acceler.ocdl.model;


import acceler.ocdl.CONSTANTS;
import acceler.ocdl.utils.SerializationUtils;

import java.io.File;

import acceler.ocdl.dto.ProjectConfigurationDto;

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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getGitRepoURI() {
        return gitRepoURI;
    }

    public void setGitRepoURI(String gitRepoURI) {
        this.gitRepoURI = gitRepoURI;
    }

    public String getK8MasterUri() {
        return k8MasterUri;
    }

    public void setK8MasterUri(String k8MasterUri) {
        this.k8MasterUri = k8MasterUri;
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

    public List<String> getSuffixes() {
        return suffixes;
    }

    public void setSuffixes(List<String> suffixes) {
        this.suffixes = suffixes;
    }

    public Project() {
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
        setSuffixesOfProjectInStorage(projectInfo.suffixes);
        lock.writeLock().unlock();
    }

    public static void setSuffixesOfProjectInStorage(List<String> newSuffixes) {
        lock.writeLock().lock();
        projectData.suffixes.clear();
        projectData.suffixes.addAll(newSuffixes);
        lock.writeLock().unlock();
    }


    public static String getProjectNameInStorage() {
        lock.readLock().lock();
        String name = projectData.projectName;
        lock.readLock().unlock();

        return name;
    }

    public static String getGitRepoURIInStorage() {
        lock.readLock().lock();
        String gitURL = projectData.gitRepoURI;
        lock.readLock().unlock();

        return gitURL;
    }

    public static String getK8MasterUriInStorage() {
        lock.readLock().lock();
        String K8MasterURL = projectData.k8MasterUri;
        lock.readLock().unlock();

        return K8MasterURL;
    }

    public static String getTemplatePathInStorage() {
        lock.readLock().lock();
        String templatePath = projectData.templatePath;
        lock.readLock().unlock();

        return templatePath;
    }

    public static String getDescriptionInStorage() {
        lock.readLock().lock();
        String description = projectData.description;
        lock.readLock().unlock();

        return description;
    }

    public static String[] getModelFileSuffixesInStorage() {
        lock.readLock().lock();
        List<String> suffixes = projectData.suffixes;
        lock.readLock().unlock();
        return suffixes.toArray(new String[0]);
    }


    private static void persistence(){
        lock.writeLock().lock();
        File dumpFile = new File(CONSTANTS.PERSISTENCE.PROJECT);
        SerializationUtils.dump(projectData, dumpFile);
        lock.writeLock().lock();

    public ProjectConfigurationDto convert2ProjectDto(List<Algorithm> algorithms) {
        String algorithmsStr = "";
        String suffixesStr = "";
        ProjectConfigurationDto projectDto = new ProjectConfigurationDto();
        /**
         * Convert algorithm list in to String with ";" to seprate each algorithm
         */
        for (Algorithm algr : algorithms) {
            algorithmsStr += algr.getAlgorithmName() + ";";
        }
        /**
         * Convert suffixes list in to String with ";" to seprate each suffixes
         */
        for (String str : suffixes) {
            suffixesStr += str + ";";
        }

        projectDto.setAlgorithm(algorithmsStr);
        projectDto.setSuffix(suffixesStr);
        projectDto.setGitPath(gitRepoURI);
        projectDto.setK8Url(k8MasterUri);
        projectDto.setProjectName(projectName);
        projectDto.setTemplatePath(templatePath);

        return projectDto;
    }
}
