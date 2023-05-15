package vista.emergentesPasajero;

import com.toedter.calendar.JCalendar;
import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AñadirPasajero {
    public AñadirPasajero(){
    }
    public void actionPerformed(ActionEvent e, JTable tablaPasaj) {
        // Mostrar una ventana de diálogo para ingresar los datos del nuevo miembro
        JTextField telefonoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextField direccionField = new JTextField();
        JCalendar fechaNacimientoCalendar = new JCalendar();
        JTextField emailField = new JTextField();
        JTextField dniField = new JTextField();
        JButton selecFoto = new JButton("Seleccionar");
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.addChoosableFileFilter(imgFilter);
        JComboBox<String> idAvionesCombo = new JComboBox<>();
        ConexionBD con = new ConexionBD();
        String query = "SELECT distinct(idVuelo) from vuelos ";
        ResultSet resul = con.ejecutarConsulta(query);
        while (true) {
            try {
                if (!resul.next()) break;
                idAvionesCombo.addItem(String.valueOf(resul.getInt("idVuelo")));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

        }
        ActionListener fotoChooser = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Obtener el archivo seleccionado
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
                } else {
                    JOptionPane.showMessageDialog(null, "No se seleccionó ninguna foto");
                }
            }
        };
        selecFoto.addActionListener(fotoChooser);
        Object[] message = {
                "Nombre:", nombreField,
                "Primer Apellido:", apellido1Field,
                "Segundo Apellido:", apellido2Field,
                "Teléfono:", telefonoField,
                "Dirección:", direccionField,
                "Fecha de Nacimiento:", fechaNacimientoCalendar,
                "Email:", emailField,
                "DNI: ", dniField,
                "Foto:", selecFoto,
                "ID Vuelo: ", idAvionesCombo
        };
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Nuevo Pasajero", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Obtener los datos ingresados por el usuario
            String telefono = telefonoField.getText();
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            String direccion = direccionField.getText();
            String fechanac = String.format("%tF", fechaNacimientoCalendar.getCalendar());
            String email = emailField.getText();
            String dni = dniField.getText();
            String idVuelo = (String) idAvionesCombo.getSelectedItem();
            int numeroIdVuelo = Integer.parseInt(idVuelo);

            // Obtener la ruta de la imagen seleccionada
            File selectedFile = fileChooser.getSelectedFile();
            String imagePath = selectedFile.getAbsolutePath();

            // Leer la imagen como un arreglo de bytes
            byte[] imagenBytes = leerImagenBytes(imagePath);

            // Insertar el nuevo miembro en la base de datos con la imagen
            con = new ConexionBD();
            int idAvion = 0; // Variable para guardar el valor de idVuelo
            try {
                String query2 = "SELECT idAvion FROM vuelos WHERE idVuelo = ?";
                PreparedStatement statement = con.getConexion().prepareStatement(query2);
                statement.setInt(1,numeroIdVuelo);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    idAvion = resultSet.getInt("idAvion");
                }

                PreparedStatement p = con.getConexion().prepareStatement("""
                INSERT INTO pasajeros(DNI, Nombre, Apellido1, Apellido2, fechaNacimiento, Telefono, ecorreo, Direccion,foto)
                VALUES (?,?,?,?,?,?,?,?,?);
            """);
                p.setString(1, dni);
                p.setString(2, nombre);
                p.setString(3, apellido1);
                p.setString(4, apellido2);
                p.setString(5,fechanac);
                p.setString(6, telefono);
                p.setString(7, email);
                p.setString(8, direccion);
                p.setBytes(9,imagenBytes);
                p.executeUpdate();
                String query3 = "SELECT idPasajeros from pasajeros where dni=" + "'"+dni+"'";
                String idPasajeroAA= "";
                ResultSet resul2 = con.ejecutarConsulta(query3);
                while(true){
                    try {
                        if (!resul2.next()) break;
                        idPasajeroAA = String.valueOf(resul2.getInt("idPasajeros"));
                    } catch (SQLException e2) {
                        throw new RuntimeException(e2);
                    }

                }
                PreparedStatement p2 = con.getConexion().prepareStatement("""
                INSERT INTO pasajeros_vuelos(idVuelo,idAvion,idPasajeros)
                VALUES (?,?,?);
            """);
                p2.setInt ( 1,numeroIdVuelo);
                p2.setInt ( 2,idAvion );
                p2.setInt ( 3,Integer.parseInt ( idPasajeroAA ) );
                p2.executeUpdate ();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private byte[] leerImagenBytes(String imagePath) {
        File file = new File(imagePath);
        byte[] imagenBytes = new byte[(int) file.length()];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(imagenBytes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        return imagenBytes;
    }
}
