import javax.swing.*;
import java.util.*;
import java.sql.*;
import java.util.Date;;

public class Vuelo {
    private int id;
    private String origen;
    private String destino;
    private Date   fecha;
    private int    duracion;
    private int capacidad;
    private int idAvion;
    public Vuelo(int id, String origen, String destino, Date fecha, int duracion, int capacidad, int idAvion) {
        this.id = id;
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.duracion = duracion;
        this.capacidad = capacidad;
        this.idAvion = idAvion;
    }

    public int getId() {
        return id;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public Date getFecha() {
        return fecha;
    }

    public int getDuracion() {
        return duracion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public int getIdAvion() {
        return idAvion;
    }

    public void reservarAsiento(int numAsiento, Usuario usuario) {
        // Conectar a la base de datos
        ConexionBD conexion = new ConexionBD();

        // Verificar si el asiento está disponible
        ResultSet rs = conexion.ejecutarConsulta("SELECT * FROM asientos WHERE id_vuelo = " + id + " AND num_asiento = " + numAsiento);
        try {
            if (rs.next()) {
                JOptionPane.showMessageDialog(null, "El asiento " + numAsiento + " ya está reservado.");
            } else {
                // Reservar el asiento
                conexion.ejecutarActualizacion("INSERT INTO asientos (id_vuelo, num_asiento, id_usuario) VALUES (" + id + ", " + numAsiento + ", " + usuario.getId() + ")");
                JOptionPane.showMessageDialog(null, "El asiento " + numAsiento + " ha sido reservado exitosamente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Cerrar la conexión a la base de datos
        conexion.cerrarConexion();
    }
}