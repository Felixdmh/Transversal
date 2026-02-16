package Controlador;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import persistencias.Comunidad;

public class BaseDeDatos {

    // CONEXIÓN / DESCONEXIÓN
    public Connection createConnection() throws Exception {
        Connection connection = null;

        final String PROPERTIES_PATH = "src/resource/database.properties";

        try (FileReader fr = new FileReader(PROPERTIES_PATH)) {

            Properties props = new Properties();
            props.load(fr);

            String driver = props.getProperty("database.driver");
            String url = props.getProperty("database.url");
            String user = props.getProperty("database.user");
            String password = props.getProperty("database.password");

            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, password);


            connection.setAutoCommit(false);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return connection;
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

    // CONSULTAS (SQL) - PROYECTO
    public List<Comunidad> selectComunidades(Connection connection) throws SQLException {
        List<Comunidad> comunidades = new ArrayList<>();

        String sql = "SELECT * FROM PORCENTAJES_RANGOEDAD ORDER BY NOMBRE_COMUNIDAD";

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Comunidad c = new Comunidad();
                c.setNombreComunidad(rs.getString("NOMBRE_COMUNIDAD"));
                c.setRango1_9(rs.getInt("RANGO_1_9"));
                c.setRango10_17(rs.getInt("RANGO_10_17"));
                c.setRango18_25(rs.getInt("RANGO_18_25"));
                c.setRango26_40(rs.getInt("RANGO_26_40"));
                c.setRango41_65(rs.getInt("RANGO_41_65"));
                c.setRangoMas66(rs.getInt("RANGO_MAS_66"));
                c.setTotalHabitantes(rs.getInt("TOTAL_HABITANTES"));

                comunidades.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw e;

        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return comunidades;
    }
}
