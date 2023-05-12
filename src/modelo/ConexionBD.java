package modelo;

import java.sql.*;

public class ConexionBD {
    private Connection conexion;
    private Statement sentencia;
    private ResultSet resultado;

    public ConexionBD() {
        try {
            // Establecer la conexión con la base de datos
            Class.forName("org.mariadb.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proy3te4","root","root");

            // Crear una sentencia para ejecutar una consulta
            sentencia = getConexion().createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ResultSet ejecutarConsulta(String consulta) {
        try {
            // Ejecutar la consulta y obtener el resultado
            resultado = getSentencia().executeQuery(consulta);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }


    public void cerrarConexion() {
        try {
            // Cerrar la conexión
            getConexion().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConexion() {
        return conexion;
    }

    public Statement getSentencia() {
        return sentencia;
    }
}
