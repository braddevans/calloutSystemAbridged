package workplacement.braddevans.DatabaseConnector;

import java.*;
import javax.activation.DataSource;
import java.sql.*;

import static workplacement.braddevans.CalloutGui.dbname;
import static workplacement.braddevans.CalloutGui.dbhostname;
import static workplacement.braddevans.CalloutGui.dbport;
import static workplacement.braddevans.CalloutGui.dbusername;
import static workplacement.braddevans.CalloutGui.dbpassword;

public class database {
    private static final database instance = new database();
    private DataSource datasource;
    private Connection connection;

    private static String host = dbhostname;
    private static String port = dbport;
    private static String username = dbusername;
    private static String password = dbpassword;
    private static String name = dbname;
    private String dbUrl;

    public static database getInstance() {
        return database.instance;
    }

    private database() {
        connectDB();
        setupConnection();
        createTables();
    }

    public void connectDB(){
        try{
            String url = "jdbc:mysql://localhost:3066/callout";
            String user = username;
            String pass = password;
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected");
        }catch(Exception ex){
            System.out.println(username);
            System.out.println(password);
            System.out.println(connection);
            ex.printStackTrace();
        }
    }

    private void setupConnection() {
        try {
            this.connection = DriverManager.getConnection(this.dbUrl, this.username, this.password);
        } catch (SQLException e) {
            System.out.println("Database Error");
        }
    }

    private void createTables() {
        //callouts
        String sql = "CREATE TABLE IF NOT EXISTS " + dbname + "Messages (" +
                "message_id int(3) NOT NULL," +
                "message varchar(255) NOT NULL," +
                "UserId int(20) NOT NULL," +
                "UserLocation varchar(255) DEFAULT 'Huddersfield University'," +
                "date-created date NOT NULL" +
                "PRIMARY KEY (message_id))";
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
}
