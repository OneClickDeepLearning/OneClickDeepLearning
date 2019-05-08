package acceler.ocdl.dto;

import acceler.ocdl.model.Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectConfigurationDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectName;
    private String k8Url;
    private String templatePath;
    private String gitPath;

    /**
     * suffixes split by ';'
     */
    private String suffix;
    private String modelTypes;
    private String algorithm;

    private Boolean forceRemoved = false;

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

    public String getGitPath() {
        return gitPath;
    }

    public void setGitPath(String gitPath) {
        this.gitPath = gitPath;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getModelTypes() {
        return modelTypes;
    }

    public void setModelTypes(String modelTypes) {
        this.modelTypes = modelTypes;
    }

    public Project convert2Project() {
        List<String> suffixesList = new ArrayList<String>();
        String[] suffixesStr = suffix.split(";");

        /**
         * convert suffixesStr into suffixes List;
         */
        for (int i = 0; i < suffixesStr.length; i++) {
            if (!"".equals(suffixesStr[i]) && suffixesStr[i] != null) {
                String tempStr = suffixesStr[i].trim();
                if (!"".equals(tempStr) || tempStr != null) {
                    suffixesList.add(tempStr);
                }
            }
        }
        Project project = new Project();
        project.setTemplatePath(templatePath);
        project.setGitRepoURI(gitPath);
        project.setK8MasterUri(k8Url);
        project.setProjectName(projectName);

        project.setSuffixes(suffixesList);

        return project;
    }

    public List<String> getAlgorithmStrList() {
        List<String> algorithmsList = new ArrayList<String>();
        String algr[] = this.algorithm.split(";");
        for (int i = 0; i < algr.length; i++) {
            String tempStr = algr[i].trim();
            if (!"".equals(tempStr) && tempStr != null) {
                algorithmsList.add(tempStr);
            }
        }
        return algorithmsList;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Boolean getForceRemoved() {
        return forceRemoved;
    }

    public void setForceRemoved(Boolean forceRemoved) {
        this.forceRemoved = forceRemoved;
    }
}
