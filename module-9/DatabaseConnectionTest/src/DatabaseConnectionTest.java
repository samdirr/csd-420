import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/** CSD 420, Module 9.2: verify the local MySQL installation through JDBC. */
public class DatabaseConnectionTest {
    public static void main(String[] args) throws Exception {
        String password = System.getenv("CSD420_DB_PASSWORD");
        if (password == null) {
            JPasswordField field = new JPasswordField();
            int choice = JOptionPane.showConfirmDialog(null, field,
                    "Enter the student1 database password", JOptionPane.OK_CANCEL_OPTION);
            if (choice != JOptionPane.OK_OPTION) return;
            char[] characters = field.getPassword();
            password = new String(characters);
            Arrays.fill(characters, '\0');
        }

        // Port 3307 selects the separate MySQL 5.7 installation on this Mac.
        String url = "jdbc:mysql://127.0.0.1:3307/databasedb"
                + "?useSSL=false&serverTimezone=UTC&connectTimeout=5000";
        System.out.println("CSD 420 - Module 9.2 - Database Connection Test");
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JDBC URL: " + url);

        // Opening a connection verifies the driver, server, database and login.
        try (Connection connection = DriverManager.getConnection(url, "student1", password);
             Statement statement = connection.createStatement()) {
            try (ResultSet result = statement.executeQuery(
                    "SELECT VERSION(), DATABASE(), CURRENT_USER(), 1 + 1")) {
                result.next();
                String version = result.getString(1);
                String database = result.getString(2);
                String user = result.getString(3);
                int answer = result.getInt(4);
                System.out.println("MySQL server version: " + version);
                System.out.println("Database: " + database);
                System.out.println("Connected user: " + user);
                System.out.println("Test query (1 + 1): " + answer);
                System.out.println("JDBC driver: " + connection.getMetaData().getDriverName()
                        + " " + connection.getMetaData().getDriverVersion());
                if (!version.startsWith("5.") || !database.equals("databasedb")
                        || !user.startsWith("student1@") || answer != 2) {
                    throw new IllegalStateException("Assignment setup verification failed.");
                }
            }
            // A temporary table verifies table creation, inserts and reads without
            // leaving test records in the assignment database.
            statement.executeUpdate("CREATE TEMPORARY TABLE setup_check (id INT PRIMARY KEY, note VARCHAR(40))");
            statement.executeUpdate("INSERT INTO setup_check VALUES (1, 'Java connection successful')");
            try (ResultSet result = statement.executeQuery("SELECT note FROM setup_check WHERE id = 1")) {
                if (!result.next()) throw new IllegalStateException("Test row was not found.");
                System.out.println("Table write/read: " + result.getString(1));
            }
            System.out.println("SUCCESS: Java connected to MySQL and all checks passed.");
        }
    }
}
