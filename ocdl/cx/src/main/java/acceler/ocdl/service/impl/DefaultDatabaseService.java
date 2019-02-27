package acceler.ocdl.service.impl;

import acceler.ocdl.model.Model;
import acceler.ocdl.model.Project;
import acceler.ocdl.model.Template;
import acceler.ocdl.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

@Component
public class DefaultDatabaseService {

    private Connection con;

    @Value("${database.mysql.driver}")
    private String driver;

    @Value("${database.mysql.url}")
    private String url;

    @Value("${database.mysql.user}")
    private String user;

    @Value("${database.mysql.password}")
    private String password;

    public DefaultDatabaseService() { }

    public void setDriver(String driver) { this.driver = driver; }

    public void setUrl(String url) { this.url = url; }

    public void setUser(String user) { this.user = user; }

    public void setPassword(String password) { this.password = password; }

    public void createConn(){
        try {
            //加载驱动程序
            System.out.println(driver);
            Class.forName(driver);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);

            if(!con.isClosed()) System.out.println("Succeeded connecting to the Database!");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //==================================================================================
    // operation of table user_role
    //==================================================================================

    /*
     * insert a new role in the table user_role
     */
    public int createNewRole(User.Role role) {

        int id = -1;
        String query = " insert into user_role (role, active)" + " values (?, ?)";

        try {

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, role.name().toLowerCase()); //name
            preparedStmt.setBoolean(2, true); //active

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }


    /*
     * get the role id in the table user_role
     */
    public int getRoleId (User.Role role) {

        int id = -1;
        String query = "select id from user_role where " + " role=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, role.name().toLowerCase()); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (id < 0) id = createNewRole(role);
        return id;
    }

    /*
     * get the role id in the table user_role
     */
    public String getRoleName (int roleId) {

        String roleName = "";
        String query = "select role from user_role where " + " id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, roleId); //id

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) roleName = rs.getString("role");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return roleName;
    }

    //==================================================================================
    // operation of table user
    //==================================================================================

    /*
     * insert a new user into table user
     */
    public int createUser(User user) {

        int id = -1;

        // create a sql date object so we can use it in our INSERT statement
        Calendar calendar = Calendar.getInstance();
        java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

        int roleId = getRoleId(user.getRole());

        // the mysql insert statement
        String query = "insert into user (name, password, role, account, type, created_time, active)" + " values (?, ?, ?, ?, ?, ?, ?)";

        try {

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, user.getUserName()); //name
            preparedStmt.setString (2, user.getPassword()); // password
            preparedStmt.setInt(3, roleId); // role
            preparedStmt.setString   (4, user.getAccount()); //account
            preparedStmt.setInt(5, user.getType()); //type
            preparedStmt.setDate    (6, startDate); // created_ time
            preparedStmt.setBoolean(7, true); //active

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    /*
     * Get user info except the password
     */
    public Long getUserId(String userName) {

       Long id = -1L;

        String query = "select id from user where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, userName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) id = rs.getLong(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }


    /*
     * Get user info except the password
     */
    public User getUserInfo(String userName) {

        User user = new User();

        String query = "select * from user where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, userName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

            rs.next();
            User.Role role = User.Role.getRole(getRoleName(rs.getInt("role")));

            ////Long userId, String userName, String account, String password, int type, Role role
            user.setUserId(rs.getLong("id"));
            user.setUserName(rs.getString("name"));
            user.setAccount(rs.getString("account"));
            user.setType(rs.getInt("type"));
            user.setRole(role);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    /*
     * get the user password
     * TODO: password need to be encrypted
     */
    public String getUserPassword(String userName) {

        String password = "";

        String query = "select password from user where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, userName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) password = rs.getString("password");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return password;
    }


    //==================================================================================
    // operation of table project
    //==================================================================================

    /*
     * insert a new project in the table project
     */
    public int createProject(String projectName, String description) {

        int id = -1;
        String query = " insert into project (name, desp)" + " values (?, ?)";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, projectName); //name
            preparedStmt.setString (2, description); //name

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }


    /*
     * get the project id in the table project
     */
    public int getProjectId (String projectName) {

        int id = -1;
        String query = "select id from project where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, projectName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Project getProjectInfo(String projectName) {

        Project project = new Project();
        String query = "select * from project where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, projectName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                project.setProjectId(rs.getInt("id"));
                project.setProjectName(rs.getString("name"));
                project.setGit(rs.getString("git"));
                project.setK8Url(rs.getString("k8_url"));
                project.setTemplateUrl(rs.getString("template_url"));
                project.setDesp(rs.getString("desp"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    public Project getProjectInfo(int projectId) {

        Project project = new Project();
        String query = "select * from project where " + " id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, projectId); //projectId

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                project.setProjectId(rs.getInt("id"));
                project.setProjectName(rs.getString("name"));
                project.setGit(rs.getString("git"));
                project.setK8Url(rs.getString("k8_url"));
                project.setTemplateUrl(rs.getString("template_url"));
                project.setDesp(rs.getString("desp"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return project;
    }

    public void setProjectGit(String git, int projectId) {

        String query = "update project set " + "git=?" + " where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, git); //git
            preparedStmt.setInt(2, projectId); // projectId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setProjectK8(String k8Url, int projectId) {

        String query = "update project set " + "k8_url=?" + " where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, k8Url); //k8Url
            preparedStmt.setInt(2, projectId); // projectId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setProjectTemplate(String templateUrl, int projectId) {

        String query = "update project set " + "template_url=?" + " where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, templateUrl); //templateUrl
            preparedStmt.setInt(2, projectId); // projectId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //==================================================================================
    // operation of table user_project_relation
    //==================================================================================

    public void createUserProjectRelation(User user, String projectName) {

        // get user id
        if (user.getUserId() == null) user.setUserId(getUserId(user.getUserName()));
        // get project id
        int projectId = getProjectId(projectName);

        String query = " insert into user_project_relation (user_id, project_id)" + " values (?, ?)";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setLong (1, user.getUserId()); //user_id
            preparedStmt.setInt(2, projectId);

            // execute the preparedstatement
            preparedStmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public ArrayList<Project> getProjectList(Long userId) {

        ArrayList<Project> projectList = new ArrayList<Project>();

        String query = "select project_id from user_project_relation where " + " user_id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setLong (1, userId); //userId

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {

                Project project = getProjectInfo(rs.getInt("project_id"));
                projectList.add(project);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return projectList;
    }

    public User getProjectManager(int projectId) {

        User user = new User();
        String userName = "";

        String query = "select user_name from user_project_view where " + " project_id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt(1, projectId); //projectId

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
           if (rs.next()) userName = rs.getString("user_name");
           user = getUserInfo(userName);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return user;
    }

    //==================================================================================
    // operation of table model_type
    // column: id, name, project_id
    //==================================================================================

    public int createModelType(String modelTypeName, int projectId) {

        int id = -1;
        String query = " insert into model_type (name, project_id)" + " values (?, ?)";

        try {

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, modelTypeName); //modelTypeName
            preparedStmt.setInt(2, projectId); //projectId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }


    public ArrayList<String> getModelType(int projectId) {

        ArrayList<String> modelTypeList = new ArrayList<String>();

        String query = "select name from model_type where " + " project_id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, projectId); //projectId

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
               modelTypeList.add(rs.getString("name"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return modelTypeList;
    }

    /*
     * get the model type id in the table model_type
     */
    public int getModelTypeId (int projectId, String modelTypeName) {

        int id = -1;
        String query = "select id from model_type where " + " name=?" + " and " + "project_id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, modelTypeName); //name
            preparedStmt.setInt (2, projectId); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    //==================================================================================
    // operation of table status
    // column: id, name
    //==================================================================================

    /*
     * get status id in the table status
     */
    public int getStatusId (Model.Status status) {

        int id = -1;
        String query = "select id from status where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, status.name().toLowerCase()); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    //==================================================================================
    // operation of table model
    // column: id, name, model_type_id, project_id, url, status_id, version
    //==================================================================================

    public int createModel(Model model) {

        int id = -1;
        String query = " insert into model (name, model_type_id, project_id, url, status_id)" + " values (?, ?, ?, ?, ?)";

        int projectId = getProjectId(model.getProject());
        int modelTypeId = getModelTypeId(projectId, model.getModelType());
        int statusId = getStatusId(model.getStatus());

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, model.getModelName()); //modelName
            preparedStmt.setInt(2, modelTypeId); //modelTypeId
            preparedStmt.setInt(3, projectId); //modelProjectId
            preparedStmt.setString(4, model.getUrl()); //modelUrl
            preparedStmt.setInt(5, statusId); //modelTypeId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public void updateModelStatus(Model model, Model.Status expectedStatus) {

        if (model.getModelId() != null) {
            updateModelStatusWithModelId(model.getModelId(), expectedStatus);
        } else {
            updateModelStatusWithoutModelId(model, expectedStatus);
        }
    }

    private void updateModelStatusWithModelId(Long modelId, Model.Status expectedStatus) {

        int statusId = getStatusId(expectedStatus);

        String query = "update model set " + "status_id=?" + " where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, statusId); //status_id
            preparedStmt.setLong(2, modelId); // modelId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void updateModelStatusWithoutModelId(Model model, Model.Status expectedStatus) {

        int statusId = getStatusId(expectedStatus);
        int projectId = getProjectId(model.getProject());
        int modelTypeId = getModelTypeId(projectId, model.getModelType());

        String query = "update model set " + "status_id=?" + " where " + "name=?" + " and " + "project_id=?" + " and " + "model_type_id=?" ;

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setInt (1, statusId); //status_id
            preparedStmt.setString(2, model.getModelName()); // modelName
            preparedStmt.setInt (3, projectId); //project_id
            preparedStmt.setInt (4, modelTypeId); //model_type_id

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
     * TODO: big version or small version
     */
    public void updateModelVersion(Model model, String version) {

        if (model.getModelId() != null) {
            updateModelVersionWithModelId(model.getModelId(), version);
        } else {
            updateModelVersionWithoutModelId(model, version);
        }
    }

    private void updateModelVersionWithModelId(Long modelId, String version) {

        String query = "update model set " + "version=?" + " where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, version); //version
            preparedStmt.setLong(2, modelId); // modelId

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    private void updateModelVersionWithoutModelId(Model model,String version) {

        int projectId = getProjectId(model.getProject());
        int modelTypeId = getModelTypeId(projectId, model.getModelType());

        String query = "update model set " + "version=?" + " where " + "name=?" + " and " + "project_id=?" + " and " + "model_type_id=?" ;

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, version); //version
            preparedStmt.setString(2, model.getModelName()); // modelName
            preparedStmt.setInt (3, projectId); //project_id
            preparedStmt.setInt (4, modelTypeId); //model_type_id

            // execute the preparedstatement
            preparedStmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //==================================================================================
    // operation of view model_view
    // column: model_id, model_name, url, version, model_type_name, project_name, status
    //==================================================================================

    public ArrayList<Model> getAllProjectModel(String projectName) {

        ArrayList<Model> modelList = new ArrayList<Model>();

        String query = "select * from model_view where " + " project_name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, projectName); //projectName

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                Model model = new Model();
                model.setModelId(rs.getLong("model_id"));
                model.setModelName(rs.getString("model_name"));
                model.setModelType(rs.getString("model_type_name"));
                model.setProject(rs.getString("project_name"));
                model.setUrl(rs.getString("url"));
                model.setStatus(Model.Status.getStatus(rs.getString("status")));
                model.setVersion(rs.getString("version"));

                modelList.add(model);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return modelList;
    }

    /*
     * get project models on specific status
     * @para condition specific status, such as: new/approval/reject
     */
    public ArrayList<Model> getConditioanalProjectModel(String projectName, Model.Status condition) {

        ArrayList<Model> modelList = new ArrayList<Model>();

        String query = "select * from model_view where " + " project_name=?" + " and " + "status=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, projectName); //projectName
            preparedStmt.setString (2, condition.name().toLowerCase()); //condition

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                Model model = new Model();
                model.setModelId(rs.getLong("model_id"));
                model.setModelName(rs.getString("model_name"));
                model.setModelType(rs.getString("model_type_name"));
                model.setProject(rs.getString("project_name"));
                model.setUrl(rs.getString("url"));
                model.setStatus(Model.Status.getStatus(rs.getString("status")));
                model.setVersion(rs.getString("version"));

                modelList.add(model);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return modelList;
    }


    //==================================================================================
    // operation of table template
    // column: id, name, file, model_suffix, desp
    //==================================================================================

    public int createTemplate(Template template) {
        int id = -1;

        Blob file = null;
        try {
            file = con.createBlob();
            file.setBytes(1, template.getFile());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = " insert into template (name, file, model_suffix, desp)" + " values (?, ?, ?, ?)";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString (1, template.getTemplateName()); //name
            preparedStmt.setBlob(2, file); //file
            preparedStmt.setString (3, template.getSuffix()); //suffix
            preparedStmt.setString (4, template.getDesp()); //description

            // execute the preparedstatement
            preparedStmt.executeUpdate();
            ResultSet rs = preparedStmt.getGeneratedKeys();
            if (rs.next()) id = rs.getInt(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public Template getTemplateInfo(Long templateId) {

        Template template = new Template();
        String query = "select * from template where " + "id=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setLong (1, templateId); //id

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                template.setTemplateId(templateId);
                template.setTemplateName(rs.getString("name"));

                Blob blob = rs.getBlob("file");
                int blobLength = (int) blob.length();
                template.setFile(blob.getBytes(1, blobLength));

                template.setSuffix(rs.getString("model_suffix"));
                template.setDesp(rs.getString("desp"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return template;
    }

    public Template getTemplateInfo(String templateName) {

        Template template = new Template();
        String query = "select * from template where " + "name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString(1, templateName); //id

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                template.setTemplateId(rs.getLong("id"));
                template.setTemplateName(templateName);

                Blob blob = rs.getBlob("file");
                int blobLength = (int) blob.length();
                template.setFile(blob.getBytes(1, blobLength));

                template.setSuffix(rs.getString("model_suffix"));
                template.setDesp(rs.getString("desp"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return template;
    }

}
