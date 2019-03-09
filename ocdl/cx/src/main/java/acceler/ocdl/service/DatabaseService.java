//package acceler.ocdl.service;
//
//import acceler.ocdl.dto.ProjectConfigurationDto;
//import acceler.ocdl.exception.DatabaseException;
//import acceler.ocdl.model.Model;
//import acceler.ocdl.model.Project;
//import acceler.ocdl.model.Template;
//import acceler.ocdl.model.User;
//
//import java.util.ArrayList;
//
//public interface DatabaseService {
//
//    void createConn();
//
//    int createNewRole(User.Role role);
//
//    int getRoleId(User.Role role);
//
//    String getRoleName(int roleId);
//
//    Long getUserId(String userName);
//
//    User getUserInfo(String userName);
//
//    String getUserPassword(String userName);
//
//    int createProject(String projectName) throws DatabaseException;
//
//    int getProjectId(String projectName) throws DatabaseException;
//
//    Project getProjectInfo(String projectName);
//
//    Project getProjectInfo(Long projectId);
//
//    void updateProject(Long projectId, String projectName, String git, String k8, String template) throws DatabaseException;
//
//    Boolean setProjectGit(String git, Long projectId);
//
//    Boolean setProjectK8(String k8Url, Long projectId);
//
//    Boolean setProjectTemplate(String templateUrl, Long projectId);
//
//    void setProjectName(String projectName, Long projectId) throws DatabaseException;
//
//    void createUserProjectRelation(User user, String projectName) throws DatabaseException;
//
//    ArrayList<ProjectConfigurationDto> getProjectList(Long userId) throws DatabaseException;
//
//    User getProjectManager(Long projectId);
//
//    int createModelType(String modelTypeName, Long projectId);
//
//    ArrayList<String> getModelType(Long projectId) throws DatabaseException;
//
//    int getModelTypeId(Long projectId, String modelTypeName) throws DatabaseException;
//
//    int getStatusId(Model.Status status);
//
//    int getStatusId(String status);
//
//    int createModel(Model model) throws DatabaseException;
//
//    void updateModelStatus(Model model, Model.Status expectedStatus) throws DatabaseException;
//
//    void updateModelStatusWithModelId(Long modelId, Model.Status expectedStatus) throws DatabaseException;
//
//    void updateModelStatusWithModelId(Long modelId, int modelTypeId, int statusId, int bigVersion, int smallVersion) throws DatabaseException;
//
//    int getLatestBigVersion(Long projectId, int modelTypeId) throws DatabaseException;
//
//    int getLatestSmallVersion(Long projectId, int modelTypeId) throws DatabaseException;
//
//    ArrayList<Model> getAllProjectModel(String projectName);
//
//    ArrayList<Model> getConditioanalProjectModel(Long projectId, Model.Status condition) throws DatabaseException;
//
//    int createTemplate(Template template);
//
//    Template getTemplateInfo(Long templateId);
//
//    Template getTemplateInfo(String templateName);
//}
