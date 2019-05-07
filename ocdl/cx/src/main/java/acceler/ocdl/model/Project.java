package acceler.ocdl.model;

import acceler.ocdl.CONSTANTS;
import acceler.ocdl.dto.ProjectConfigurationDto;
import acceler.ocdl.exception.InitStorageException;
import acceler.ocdl.exception.NotFoundException;
import acceler.ocdl.utils.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Project extends Storable implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Project.class);

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static Project projectDataStorage;


    private static Project getProjectDataStorage() {
        if (projectDataStorage == null) {
            logger.error("ProjectDataStorage instance is null");
            throw new InitStorageException("ProjectDataStorage instance is null");
        }

        return projectDataStorage;
    }

    static void initializeStorage() {
        if (projectDataStorage == null) {
            logger.info("[init] RejectedModelStorage instance initialization executed");
            File projectDataFile = new File(CONSTANTS.PERSISTENCE.PROJECT);
            try {
                projectDataStorage = (Project) StorageLoader.loadStorage(projectDataFile);
            } catch (NotFoundException nfe) {
                projectDataStorage = new Project();
            }
        }

        logger.warn("Storage initialization only allow been executed at init time");
    }

    private static void persistence() {
        File dumpFile = new File(CONSTANTS.PERSISTENCE.PROJECT);
        SerializationUtils.dump(projectDataStorage, dumpFile);
    }

    public static Project getProjectInStorage() {
        return getProjectDataStorage().deepCopy();
    }

    public static void setProjectDataStorage(Project projectInfo) {
        Project realProjectData = getProjectDataStorage();

        lock.writeLock().lock();
        realProjectData.projectName = projectInfo.projectName;
        realProjectData.gitRepoURI = projectInfo.gitRepoURI;
        realProjectData.k8MasterUri = projectInfo.k8MasterUri;
        realProjectData.templatePath = projectInfo.templatePath;
        realProjectData.description = projectInfo.description;

        setSuffixesOfProjectInStorage(projectInfo.suffixes);
        persistence();

        lock.writeLock().unlock();
    }

    public static void setSuffixesOfProjectInStorage(List<String> newSuffixes) {
        lock.writeLock().lock();
        getProjectDataStorage().suffixes.clear();
        getProjectDataStorage().suffixes.addAll(newSuffixes);
        persistence();
        lock.writeLock().unlock();
    }


    public static String getProjectNameInStorage() {
        lock.readLock().lock();
        String name = getProjectDataStorage().projectName;
        lock.readLock().unlock();

        return name;
    }

    public static String getGitRepoURIInStorage() {
        lock.readLock().lock();
        String gitURL = getProjectDataStorage().gitRepoURI;
        lock.readLock().unlock();

        return gitURL;
    }

    public static String getK8MasterUriInStorage() {
        lock.readLock().lock();
        String K8MasterURL = getProjectDataStorage().k8MasterUri;
        lock.readLock().unlock();

        return K8MasterURL;
    }

    public static String getTemplatePathInStorage() {
        lock.readLock().lock();
        String templatePath = getProjectDataStorage().templatePath;
        lock.readLock().unlock();

        return templatePath;
    }

    public static String getDescriptionInStorage() {
        lock.readLock().lock();
        String description = getProjectDataStorage().description;
        lock.readLock().unlock();

        return description;
    }

    public static String[] getModelFileSuffixesInStorage() {
        lock.readLock().lock();
        List<String> suffixes = getProjectDataStorage().suffixes;
        lock.readLock().unlock();
        return suffixes.toArray(new String[0]);
    }


    private String projectName;
    private String gitRepoURI;
    private String k8MasterUri;
    private String templatePath;
    private String description;
    private List<String> suffixes;


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

    public ProjectConfigurationDto convert2ProjectDto(List<Algorithm> algorithms) {
        ProjectConfigurationDto projectDto = new ProjectConfigurationDto();

        // convert algorithm list in to String with ";" to seprate each algorithm
        StringBuilder algorithmsStrBuilder = new StringBuilder();
        for (Algorithm algr : algorithms) {
            algorithmsStrBuilder.append(algr.getAlgorithmName());
            algorithmsStrBuilder.append(";");
        }

        // convert suffixes list in to String with ";" to seprate each suffixes
        StringBuilder suffixesStrBuilder = new StringBuilder();
        for (String str : suffixes) {
            suffixesStrBuilder.append(str);
            suffixesStrBuilder.append(";");
        }

        projectDto.setAlgorithm(algorithmsStrBuilder.toString());
        projectDto.setSuffix(suffixesStrBuilder.toString());
        projectDto.setGitPath(gitRepoURI);
        projectDto.setK8Url(k8MasterUri);
        projectDto.setProjectName(projectName);
        projectDto.setTemplatePath(templatePath);

        return projectDto;
    }

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
}
