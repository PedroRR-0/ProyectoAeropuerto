package vista;

import modelo.ConexionBD;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AñadirTripulante {
    public AñadirTripulante(){
    }
    public void actionPerformed( ActionEvent e,DefaultListModel<String> miembrosModel) {
        // Mostrar una ventana de diálogo para ingresar los datos del nuevo miembro
        JTextField        telefonoField     = new JTextField();
        JTextField        nombreField       = new JTextField();
        JTextField        apellido1Field    = new JTextField();
        JTextField        apellido2Field    = new JTextField();
        JTextField        direccionField    = new JTextField();
        JCalendar fechaNacimientoCalendar = new JCalendar();
        JTextField        emailField = new JTextField();
        String[]          categorias        = { "Piloto", "Copiloto", "Ingeniero de vuelo", "Azafato" };
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        Object[] message = {
                "Teléfono:", telefonoField,
                "Nombre:", nombreField,
                "Primer Apellido:", apellido1Field,
                "Segundo Apellido:", apellido2Field,
                "Dirección:", direccionField,
                "Fecha de Nacimiento:", fechaNacimientoCalendar,
                "Email:", emailField,
                "Categoría:", categoriaComboBox
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Nuevo Miembro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Obtener los datos ingresados por el usuario
            String telefono = telefonoField.getText();
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            String direccion = direccionField.getText ();
            String fechanac = String.format("%tF", fechaNacimientoCalendar.getCalendar());
            String email = emailField.getText ();
            String categoria = (String) categoriaComboBox.getSelectedItem();

            // Insertar el nuevo miembro en la base de datos
            ConexionBD conexionBD = new ConexionBD();
            conexionBD.ejecutarConsulta (String.format("INSERT INTO miembros (telefono, nombre, apellido1, apellido2, direccion, fechaNacimiento, ecorreo, categoria) VALUES ('%s', '%s', '%s', '%s', '%s', '%s','%s','%s')",
                    telefono, nombre, apellido1, apellido2, direccion, fechanac, email, categoria));
            conexionBD.cerrarConexion();

            // Actualizar la lista de miembros
            miembrosModel.clear();
            ConexionBD conexionBD2 = new ConexionBD();
            ResultSet  salida      = conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
            while ( true ) {
                try {
                    if ( ! salida.next() ) break;
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String telefonoDB = null;
                try {
                    telefonoDB = salida.getString("telefono");
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String nombreDB = null;
                try {
                    nombreDB = salida.getString("nombre");
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String apellido1DB = null;
                try {
                    apellido1DB = salida.getString("apellido1");
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String apellido2DB = null;
                try {
                    apellido2DB = salida.getString("apellido2");
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String categoriaDB = null;
                try {
                    categoriaDB = salida.getString("categoria");
                }
                catch ( SQLException ex ) {
                    throw new RuntimeException ( ex );
                }
                String miembro = telefonoDB + " " + nombreDB + " " + apellido1DB + " " + apellido2DB + " (" + categoriaDB + ")";
                miembrosModel.addElement(miembro);
            }
            conexionBD2.cerrarConexion();
        }
    }
}
