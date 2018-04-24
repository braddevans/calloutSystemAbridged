package workplacement.braddevans.DatabaseConnector;

import java.*;
import javax.activation.DataSource;
import java.sql.*;

import static workplacement.braddevans.CalloutGui.*;

public class database {
    private static final String Database_ip = dbhostname;
    private static final String Database_port = dbport;
    private static final String Database_username = dbusername;
    private static final String Database_password = dbpassword;
    private static final String Database_name = dbname;
    private static String host;
    private static String port;
    private static String username;
    private static String password;
    private static String name;
    private static String url;
    private DataSource datasource;
    private static Connection conn;

    public static void database() {
        initDatabase();
        setupConnection();
        try {
            createDatabase(name);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void initDatabase() {
        //Get config from Config in plugin
        host = (String) Database_ip;
        port = String.valueOf(Database_port);
        username = (String) Database_username;
        password = (String) Database_password;
        String name = (String) Database_name;
        dburl = "jdbc:mysql://" + host + ":" + port + "/" + name;
        System.out.println("init database");
    }

    public static void setupConnection() {
        try {
            conn = DriverManager.getConnection(dburl, username, password);
            System.out.println("Connected to: "+ dburl + " | " + dbusername + " | " + dbpassword + " | #" + dbport);
        } catch (SQLException e) {
            System.out.println("Database Error Connecting to:"+ dburl + " | " + dbusername + " | " + dbpassword + " | #" + dbport);
            e.printStackTrace();
        }
    }
    public static Connection openConnection() {
        try {
            if (conn.isClosed()) {
                setupConnection();
            }
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void createTables() throws SQLException {
        //create the table
        String sql = "CREATE TABLE IF NOT EXISTS `Messages` ( " +
                "message_id int(3) NOT NULL, " +
                "message varchar(255) NOT NULL, " +
                "UserId int(20) NOT NULL, " +
                "UserLocation varchar(255) DEFAULT 'Huddersfield University', " +
                "DateCreated date NOT NULL " +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
        createTableFromSql(sql);
        System.out.println(sql);
    }

    private static void createTableFromSql(String sql) {
        String tableName = sql.substring(sql.indexOf("NOT EXISTS ") + 11, sql.indexOf(" ("));
        System.out.println("Creating Table " + tableName + " if not exists Please Wait");
        try (Statement statement = openConnection().createStatement()) {
            statement.execute(sql);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
    private static void createDatabase(String name) throws SQLException {
        System.out.println("Creating Database Please Wait");
        try {
            name = dbname;
            Class.forName("com.mysql.jdbc.Driver");
            String url = dburl;
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            statement.execute("CREATE DATABASE IF NOT EXISTS " + name);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
