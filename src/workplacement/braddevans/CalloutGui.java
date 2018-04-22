package workplacement.braddevans;

import workplacement.braddevans.DatabaseConnector.database;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import static java.lang.System.getProperty;

public class CalloutGui extends JFrame {

    private JLabel labelitsupport = new JLabel("It Support: ");
    private JLabel labellibrary = new JLabel("Library: ");
    private JTextField textitsupport = new JTextField(20);
    private JTextField fieldlibrary = new JPasswordField(20);
    private JButton buttonSubmit = new JButton("submit");
    private static database db;

    public static String dbusername;
    public static String dbpassword;
    public static String dburl;
    public static String dbname;
    public static String dbhostname;
    public static String dbport;

    public CalloutGui(String dbusername, String dbpassword, String dburl, String dbname, String dbport, String dbhostname) {
        super("Call Out Program");
        dbusername = dbusername;
        dbpassword = dbpassword;
        dburl = dburl;
        dbname = dbname;
        dbhostname = dbhostname;
        dbport = dbport;

        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelitsupport, constraints);

        constraints.gridx = 1;
        newPanel.add(textitsupport, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        newPanel.add(labellibrary, constraints);

        constraints.gridx = 1;
        newPanel.add(fieldlibrary, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonSubmit, constraints);

        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Call Out Panel"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        //create config
        createconfig();

        //load config
        loadconfig();

        //database
        db.connectDB();

        // set look and feel to the system look and feel

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new CalloutGui(dbusername, dbpassword, dburl, dbname, dbport, dbhostname).setVisible(true);
            }
        });

    }

    public static void loadconfig() {
        Properties prop = new Properties();
        InputStream input = null;

        String userhome = getProperty("user.home");

        try {
            input = new FileInputStream(userhome + "/config.properties");
            prop.load(input);

            dbport = prop.getProperty("dbport");
            dbusername = prop.getProperty("dbusername");
            dbpassword = prop.getProperty("dbpassword");
            dbhostname = prop.getProperty("dbhostname");
            dbname = prop.getProperty("dbname");
            dburl = prop.getProperty("dburl");

            System.out.println(dburl);
            System.out.println(dbusername);
            System.out.println(dbpassword);
            System.out.println(dbname);
            System.out.println(dbport);
            System.out.println(dbhostname);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void createconfig() {
        Properties prop = new Properties();
        OutputStream output = null;

        String userhome = getProperty("user.home");

        try {
            output = new FileOutputStream(userhome + "/config.properties");

            // set the properties value
            prop.setProperty("dbusername", "braddevans");
            prop.setProperty("dbpassword", "somepassword");
            prop.setProperty("dbhostname", "localhost");
            prop.setProperty("dbport", "3066");
            prop.setProperty("dbname", "callout");
            prop.setProperty("dburl", "jdbc:mysql://localhost:3066/callouts");

            prop.store(output, null);

            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Path path = Paths.get(userhome + "/config.properties");
            Files.createDirectories(path.getParent());
            if (!Files.exists(path)) {
                Files.createFile(path);
                Files.write(path, ("").getBytes());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}