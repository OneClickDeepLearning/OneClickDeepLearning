package acceler.ocdl.service.impl;

import acceler.ocdl.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.*;
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
    public int insertUser(User user) {

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
    public User getUserInfo(String userName) {

        User user = new User();

        String query = "select * from user where " + " name=?";

        try {
            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);
            preparedStmt.setString (1, userName); //name

            // execute the preparedstatement
            ResultSet rs = preparedStmt.executeQuery();

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
    // operation of table user
    //==================================================================================

    /*
     *
     */
    public int createProject(User user) {



    }

}
