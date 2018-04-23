package workplacement.braddevans.DatabaseConnector;

import java.*;
import javax.activation.DataSource;
import java.sql.*;

import static workplacement.braddevans.CalloutGui.*;

public class database {
    private static final database instance = new database();
    private DataSource datasource;
    private Connection connection;

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

    public static database getInstance() {
        return database.instance;
    }

    private database() {
        initDatabase();
        setupConnection();
        createTables();
    }

    public static void initDatabase() {
        //Get config from Config in plugin
        host = (String) Database_ip;
        port = String.valueOf(Database_port);
        username = (String) Database_username;
        password = (String) Database_password;
        String name = (String) Database_name;
        dburl = "jdbc:mysql://" + host + ":" + port + "/" + name;
        }

    //public static void connectDB(){
    //    try{
    //        Class.forName("com.mysql.jdbc.Driver");
    //        String url = dburl;
    //        String user = dbusername;
    //        String pass = dbpassword;
    //        Connection conn = DriverManager.getConnection(url, user, pass);
    //        System.out.println("Connected");
    //        System.out.println(dbusername);
    //        System.out.println(dbpassword);
    //        System.out.println(conn);
    //    }catch(Exception ex){
    //        ex.printStackTrace();
    //   }
    //}

    private void setupConnection() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Database Error");
        }
    }

    private void createTables() {
        //create the table
        String sql = "CREATE TABLE IF NOT EXISTS Messages (" +
                "  `message_id` int(3) NOT NULL," +
                "  `message` varchar(255) NOT NULL," +
                "  `UserId` int(20) NOT NULL," +
                "  `UserLocation` varchar(255) DEFAULT 'Huddersfield University'," +
                "  `DateCreated` date NOT NULL" +
                ") ENGINE=InnoDB DEFAULT CHARSET=latin1;" +
                "ALTER TABLE `Messages`" +
                "  ADD PRIMARY KEY (`message_id`);";
        createTableFromSql(sql);
    }

    public Connection openConnection() {
        try {
            if (this.connection.isClosed()) {
                this.setupConnection();
            }
            return this.connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createTableFromSql(String sql) {
        String tableName = sql.substring(sql.indexOf("NOT EXISTS ") + 11, sql.indexOf(" ("));
        System.out.println("Creating Table " + tableName + " if it doesn't exits Please Wait");
        try (Statement statement = this.openConnection().createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void createDatabase(String callouts) {
        System.out.println("Creating Database Please Wait");
        try {
            String dbUrl = "jdbc:mysql://" + this.host + ":" + this.port;
            Connection conn = DriverManager.getConnection(this.url, this.username, this.password);
            Statement statement = conn.createStatement();
            statement.execute("CREATE DATABASE " + callouts);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}
