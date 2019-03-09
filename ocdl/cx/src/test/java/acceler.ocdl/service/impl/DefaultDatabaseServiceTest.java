
//package acceler.ocdl.service.impl;
//
//import acceler.ocdl.OcdlApplication;
//import acceler.ocdl.model.Model;
//import acceler.ocdl.model.ProjectCrud;
//import acceler.ocdl.model.Template;
//import acceler.ocdl.model.User;
//import acceler.ocdl.crud.impl.DefaultDatabaseService;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.io.UnsupportedEncodingException;
//import java.sql.Blob;
//import java.util.ArrayList;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = {OcdlApplication.class})
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class DefaultDatabaseServiceTest {
//
//    @Autowired
//    private DefaultDatabaseService db;
//
//    private User user;
//    private Template template;
//    private Model model;
//    private ProjectCrud project;
//
//
//    @Before
//    public void setUp() throws Exception {
//        db.createConn();
//    }
//
//    //==================================================================================
//    // operation of table user_role
//    //==================================================================================
//
//    @Test
//    public void createNewRole() {
//        User.Role role = User.Role.TEST;
//        int id = db.createNewRole(role);
//        System.out.println("the new role id = " + id );
//        assertTrue(id>0);
//
//    }
//
//    @Test
//    public void getRoleId() {
//        User.Role role = User.Role.MANAGER;
//        int id = db.getRoleId(role);
//        // note increment id will be different
//        assertEquals(2, id);
//    }
//
//    @Test
//    public void getRoleName() {
//        String name = db.getRoleName(2);
//        System.out.println("the role name is: " + name);
//        assertEquals("manager", name);
//    }
//
//    //==================================================================================
//    // operation of table user
//    //==================================================================================
//
//    @Test
//    public void createUser() {
//        user = new User("test", "account", "1234", 1, User.Role.TEST);
//        int id = db.createUser(user);
//        System.out.println("the user id = " + id );
//        assertTrue(id>0);
//    }
//
//    @Test
//    public void getUserId() {
//        Long id = db.getUserId("test");
//        System.out.println("the user id is: " + id);
//        Long expected = 1L;
//        assertEquals(expected, id);
//    }
//
//    @Test
//    public void  getUserPassword() {
//        String password = db.getUserPassword("test");
//        System.out.println("the user password is: " + password);
//        assertEquals("1234", password);
//    }
//
//
//    @Test
//    public void getUserInfo() {
//        User user = db.getUserInfo("test");
//        System.out.println("the user info is: ");
//        System.out.println(user.getUserId());
//        System.out.println(user.getUserName());
//        System.out.println(user.getPassword());
//        System.out.println(user.getRole());
//
//        assertEquals(User.Role.TEST, user.getRole());
//    }
//
//    //==================================================================================
//    // operation of table project
//    //==================================================================================
//
//    @Test
//    public void createProject() {
//        int id = db.createProject("project3");
//        System.out.println("the project id is: " + id);
//        assertTrue(id>0);
//    }
//
//    @Test
//    public void getProjectId() {
//        int id = db.getProjectId("project2");
//        System.out.println("the project id is: " + id);
//        assertEquals(2, id);
//    }
//
//    @Test
//    public void getProjectInfoById() {
//
//        db.setProjectGit("www.project3.com", 3);
//        db.setProjectK8("k8", 3);
//        db.setProjectTemplate("template url", 3);
//
//        ProjectCrud project = db.getProjectInfo(3);
//        assertEquals("project3", project.getProjectName());
//        assertEquals("www.project3.com", project.getGit());
//        assertEquals("k8", project.getK8Url());
//        assertEquals("template url", project.getTemplateUrl());
//    }
//
//    @Test
//    public void getProjectInfoByName() {
//
//        ProjectCrud project = db.getProjectInfo("project3");
//        assertEquals(3, project.getProjectId());
//        assertEquals("www.project3.com", project.getGit());
//        assertEquals("k8", project.getK8Url());
//        assertEquals("template url", project.getTemplateUrl());
//
//    }
//
//
//    //==================================================================================
//    // operation of table user_project_relation
//    //==================================================================================
//
//    @Test
//    public void createUserProjectRelation() {
//
//        User user = db.getUserInfo("test");
//        db.createUserProjectRelation(user, "project2");
//
//    }
//
//    @Test
//    public void getProjectList() {
//
//        Long id = 1L;
//        ArrayList<ProjectCrud> projectList = db.getProjectList(id);
//
//        projectList.stream().forEach(p -> {
//            System.out.println(p.getProjectName());
//            System.out.println(p.getGit());
//        });
//
//        assertEquals(2, projectList.size());
//    }
//
//    @Test
//    public void getProjectManager() {
//
//        User user = db.getProjectManager(3);
//        assertEquals("test", user.getUserName());
//    }
//
//
//    //==================================================================================
//    // operation of table model_type
//    // column: id, name, project_id
//    //==================================================================================
//
//    @Test
//    public void createModelType() {
//        int id = db.createModelType("test_type222", 3);
//        System.out.println("the model type id is:" + id);
//        assertTrue(id>0);
//    }
//
//    @Test
//    public void getModelType() {
//        ArrayList<String> modelTypeList = db.getModelType(3);
//        modelTypeList.stream().forEach(m -> {
//            System.out.println(m);
//        });
//        assertEquals(2, modelTypeList.size());
//
//
//    }
//
//    @Test
//    public void getModelTypeId() {
//        int id = db.getModelTypeId(3, "test_type222");
//        System.out.println("the model type id is:" + id);
//        assertEquals(2, id);
//    }
//
//
//    //==================================================================================
//    // operation of table status
//    // column: id, name
//    //==================================================================================
//
//    @Test
//    public void getStatusId () {
//
//        int id = db.getStatusId(Model.Status.APPROVAL);
//        System.out.println("the status approval id is: " + id);
//        assertEquals(2, id);
//
//    }
//
//
//    //==================================================================================
//    // operation of table model
//    // column: id, name, model_type_id, project_id, url, status_id, version
//    //==================================================================================
//
//
//    @Test
//    public void createModel() {
//        //column: id, name, model_type, project, url, status, version
//        Model model = new Model("test_model333", "test_type222", "project3", "www");
//
//        int id = db.createModel(model);
//        assertTrue(id>0);
//    }
//
//    @Test
//    public void updateModelStatus() {
//
//        Model model = new Model("test_model444", "test_type222", "project3", "www");
//        db.updateModelStatus(model, Model.Status.APPROVAL);
////
////        Long id = 1L;
////        model.setModelId(id);
////        db.updateModelStatus(model, Model.Status.REJECT);
//
//    }
//
//
//    //==================================================================================
//    // operation of view model_view
//    // column: model_id, model_name, url, version, model_type_name, project_name, status
//    //==================================================================================
//
//    @Test
//    public void getAllProjectModel() {
//
//        ArrayList<Model> models = db.getAllProjectModel("project3");
//        models.stream().forEach(m -> {
//            System.out.println(m.getModelName());
//            System.out.println(m.getModelType());
//            System.out.println(m.getProject());
//        });
//        assertEquals(4, models.size());
//
//    }
//
//    @Test
//    public void getConditioanalProjectModel() {
//
//        ArrayList<Model> models = db.getConditioanalProjectModel(3, Model.Status.APPROVAL);
//        models.stream().forEach(m -> {
//            System.out.println(m.getModelName());
//            System.out.println(m.getModelType());
//            System.out.println(m.getProject());
//        });
//        assertEquals(1, models.size());
//
//        models = db.getConditioanalProjectModel(3, Model.Status.REJECT);
//        models.stream().forEach(m -> {
//            System.out.println(m.getModelName());
//            System.out.println(m.getModelType());
//            System.out.println(m.getProject());
//        });
//        assertEquals(1, models.size());
//
//        models = db.getConditioanalProjectModel(3, Model.Status.NEW);
//        models.stream().forEach(m -> {
//            System.out.println(m.getModelName());
//            System.out.println(m.getModelType());
//            System.out.println(m.getProject());
//        });
//        assertEquals(2, models.size());
//
//    }
//
//    //==================================================================================
//    // operation of table template
//    // column: id, name, file, model_suffix, desp
//    //==================================================================================
//
//    @Test
//    public void createTemplate() {
//
//        String content = "this is a test";
//        byte[] file;
//        try {
//            file = content.getBytes("UTF-8");
//            template = new Template("test_template", file, ".mod", "this is a test");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        int id = db.createTemplate(template);
//        System.out.println("the new template id = " + id );
//        assertTrue(id>0);
//    }
//
//    @Test
//    public void getTemplateInfoById() {
//
//        Long id = 1L;
//        Template template = db.getTemplateInfo(id);
//        System.out.println(template.getTemplateName());
//        System.out.println(template.getSuffix());
//        System.out.println(template.getDesp());
//        assertEquals("test_template", template.getTemplateName());
//    }
//
//    @Test
//    public void getTemplateInfoByName() {
//
//        Template template = db.getTemplateInfo("test_template");
//        System.out.println(template.getTemplateName());
//        System.out.println(template.getSuffix());
//        System.out.println(template.getDesp());
//        assertEquals(".mod", template.getSuffix());
//
//    }
//
//
//    @Configuration
//    @ComponentScan(basePackages = {"acceler.ocdl.crud"})
//    public class Testconfig{
//
//    }
//}
