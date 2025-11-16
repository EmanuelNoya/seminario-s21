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
        // intenta leer el archivo de propiedades si está presente
        try (InputStream in = DBConnection.class.getResourceAsStream("/conexionDB/db.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                URL = p.getProperty("db.url", URL);
                USER = p.getProperty("db.user", USER);
                PASSWORD = p.getProperty("db.password", PASSWORD);
            }
        } catch (Exception e) {
            // ignorar y usar valores por defecto
        }
    }

    public static Connection getConnection() throws Exception {
        // Asegura que el driver JDBC de MySQL esté disponible en tiempo de ejecución (mysql-connector-java)
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (java.sql.SQLException e) {
            // Mensaje claro para quien invoque la conexión
            String msg = "No se pudo conectar a la base de datos: " + e.getMessage();
            System.err.println(msg);
            throw new Exception(msg, e);
        }
    }

    /**
     * Prueba rápida de conectividad a la base de datos. Devuelve true si puede abrir una conexión.
     */
    public static boolean testConnection() {
        try (Connection c = DriverManager.getConnection(URL, USER, PASSWORD)) {
            return true;
        } catch (java.sql.SQLException e) {
            System.err.println("testConnection: no se pudo conectar a la BD: " + e.getMessage());
            return false;
        }
    }
}
