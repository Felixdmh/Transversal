package Controlador;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class BaseDeDatos {

    public Connection createConnection() throws Exception {
        Connection connection = null;

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new IllegalStateException("No se encontró database.properties en src/main/resources");
            }

            Properties props = new Properties();
            props.load(is);

            String driver = props.getProperty("database.driver");
            String url = props.getProperty("database.url");
            String user = props.getProperty("database.user");
            String password = props.getProperty("database.password");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            return connection;

        } catch (Exception e) {
            e.printStackTrace();
            throw e; // relanzamos la excepción concreta
        }
    }

    public void disconnect(Connection connection) throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}