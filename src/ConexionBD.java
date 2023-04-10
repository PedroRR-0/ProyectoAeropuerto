import java.sql.*;

public class ConexionBD {
    private Connection conn;

    public ConexionBD() {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/aeropuerto", "usuario", "contraseña");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet ejecutarConsulta(String consulta) {
        ResultSet rs = null;
        try {
            Statement stmt = conn.createStatement();
            rs = stmt.executeQuery(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    public void ejecutarActualizacion(String consulta) {
        try {
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(consulta);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void cerrarConexion() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
