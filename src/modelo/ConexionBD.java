package modelo;

import javax.swing.*;
import java.awt.*;
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

    public ImageIcon obtenerImagen(int telefono) throws SQLException {
        // Realiza la consulta para obtener la imagen por el ID
        ResultSet resultado = ejecutarConsulta("SELECT foto FROM miembros WHERE telefono = " + telefono);

        // Si hay un resultado, obtén la imagen como un byte array
        if (resultado.next()) {
            byte[] imagenBytes = resultado.getBytes("foto");

            // Convierte el byte array en un ImageIcon
            ImageIcon imagenIcono = new ImageIcon(imagenBytes);

            // Escala la imagen al tamaño deseado si es necesario
            Image imagen = imagenIcono.getImage().getScaledInstance(150, 150, Image.SCALE_DEFAULT);

            // Crea un nuevo ImageIcon escalado y lo devuelve
            return new ImageIcon(imagen);
        }

        // Si no hay resultado, devuelve null
        return null;
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
