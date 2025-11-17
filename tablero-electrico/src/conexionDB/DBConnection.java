package conexionDB;

import java.io.InputStream;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnection {
    private static String URL = "jdbc:mysql://localhost:3306/tableros?useSSL=false&serverTimezone=UTC";
    private static String USER = "root";
    private static String PASSWORD = "";

    static {
        // intenta leer el archivo de propiedades si está presente
        try {
            // Primero intenta desde el classpath (cuando está compilado)
            InputStream in = DBConnection.class.getResourceAsStream("/conexionDB/db.properties");
            
            // Si no encuentra desde classpath, intenta desde src
            if (in == null) {
                java.io.File propFile = new java.io.File("src/conexionDB/db.properties");
                if (propFile.exists()) {
                    in = new java.io.FileInputStream(propFile);
                }
            }
            
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                in.close();
                URL = p.getProperty("db.url", URL);
                USER = p.getProperty("db.user", USER);
                PASSWORD = p.getProperty("db.password", PASSWORD);
                System.out.println("[OK] Propiedades cargadas desde db.properties");
                System.out.println("    URL: " + URL);
                System.out.println("    Usuario: " + USER);
            }
        } catch (Exception e) {
            // ignorar y usar valores por defecto
            System.err.println("[ADVERTENCIA] No se pudo leer db.properties: " + e.getMessage());
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
}
