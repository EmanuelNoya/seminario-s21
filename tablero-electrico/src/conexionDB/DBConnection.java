package conexionDB;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/tableros?useSSL=false&serverTimezone=UTC";
    private static String USER = "root";
    private static String PASSWORD = "";

    static {
        // try to load properties file if present
        try (InputStream in = DBConnection.class.getResourceAsStream("/conexionDB/db.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                URL = p.getProperty("db.url", URL);
                USER = p.getProperty("db.user", USER);
                PASSWORD = p.getProperty("db.password", PASSWORD);
            }
        } catch (Exception e) {
            // ignore, use defaults
        }
    }

    public static Connection getConnection() throws Exception {
        // Ensure the MySQL JDBC driver is available at runtime (mysql-connector-java)
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
