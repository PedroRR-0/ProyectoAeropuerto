package vista.emergentesTripulante;

import controlador.Logomens;
import modelo.ConexionBD;
import com.toedter.calendar.JCalendar;
import vista.PestañaTripulantes;
import vista.emergentesPasajero.EditarPasajero;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditarTripulante {
    URL imgUrl = getClass().getResource("/default.png");
    InputStream inputStream = imgUrl.openStream();
    public EditarTripulante() throws IOException {
    }

    public void actionPerformed(ActionEvent e, String selected, DefaultListModel<String> miembrosModel) throws IOException {
        // Obtener el miembro seleccionado de la lista

        // Extraer el número de teléfono del miembro seleccionado
        String telefonoSeleccionado = selected.substring(0, selected.indexOf(" "));

        // Obtener los datos actuales del miembro desde la base de datos
        ConexionBD conexionBD = new ConexionBD();
        ResultSet resultado = conexionBD.ejecutarConsulta(String.format("SELECT * FROM miembros WHERE telefono = '%s'", telefonoSeleccionado));
        String telefono = null;
        String nombre = null;
        String apellido1 = null;
        String apellido2 = null;
        String direccion = null;
        String fechanac = null;
        String email = null;
        String categoria = null;
        try {
            if (resultado.next()) {
                telefono = resultado.getString("telefono");
                nombre = resultado.getString("nombre");
                apellido1 = resultado.getString("apellido1");
                apellido2 = resultado.getString("apellido2");
                direccion = resultado.getString("direccion");
                fechanac = resultado.getString("fechaNacimiento");
                email = resultado.getString("ecorreo");
                categoria = resultado.getString("categoria");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            conexionBD.cerrarConexion();
        }

        // Mostrar una ventana de diálogo con los datos actuales del miembro para editarlos
        JTextField telefonoField = new JTextField(telefono);
        JTextField nombreField = new JTextField(nombre);
        JTextField apellido1Field = new JTextField(apellido1);
        JTextField apellido2Field = new JTextField(apellido2);
        JTextField direccionField = new JTextField(direccion);
        JCalendar fechaNacimientoCalendar = new JCalendar();
        fechaNacimientoCalendar.setDate(java.sql.Date.valueOf(fechanac));
        JTextField emailField = new JTextField(email);
        String[] categorias = { "Piloto", "Copiloto", "Ingeniero de vuelo", "Azafato" };
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        categoriaComboBox.setSelectedItem(categoria);
        JButton selecFoto = new JButton("Seleccionar");
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.addChoosableFileFilter(imgFilter);
        selecFoto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    // Obtener el archivo seleccionado
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Archivo seleccionado: " + selectedFile.getAbsolutePath());
                } else {
                    System.out.println("No se seleccionó ningún archivo.");
                }
            }
        });
        Object[] message = {
                "Teléfono:", telefonoField,
                "Nombre:", nombreField,
                "Primer Apellido:", apellido1Field,
                "Segundo Apellido:", apellido2Field,
                "Dirección:", direccionField,
                "Fecha de Nacimiento:", fechaNacimientoCalendar,
                "Email:", emailField,
                "Categoría:", categoriaComboBox,
                "Foto:", selecFoto
        };
        //Crear un ImagenIcon personalizado
        ImageIcon cambio = new ImageIcon ( obtenerFotoTripulante(telefono));

        int option = JOptionPane.showConfirmDialog(null, message, "Editar Miembro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, cambio);
        if (option == JOptionPane.OK_OPTION) {
            // Obtener los nuevos datos ingresados por el usuario
            String nuevoTelefono = telefonoField.getText();
            String nuevoNombre = nombreField.getText();
            String nuevoApellido1 = apellido1Field.getText();
            String nuevoApellido2 = apellido2Field.getText();
            String nuevaDireccion = direccionField.getText();
            String nuevaFechaNac = String.format("%tF", fechaNacimientoCalendar.getCalendar());
            String nuevoEmail = emailField.getText();
            String nuevaCategoria = (String) categoriaComboBox.getSelectedItem();


            // Leer la foto seleccionada por el usuario y convertirla en un arreglo de bytes
            File fotoFile = fileChooser.getSelectedFile();

            byte[] fotoBytes = obtenerFotoTripulante ( telefono );
            if (fotoFile != null) {
                fotoBytes = leerBytesFoto(fotoFile);
            }

            // Actualizar los datos del miembro en la base de datos, incluyendo la foto
            ConexionBD conexion = new ConexionBD();
            PreparedStatement statement = null;
            try {
                statement = conexion.getConexion().prepareStatement("UPDATE miembros SET telefono=?, nombre=?, apellido1=?, apellido2=?, direccion=?, fechaNacimiento=?, ecorreo=?, categoria=?, foto=? WHERE telefono=?");
                statement.setString(1, nuevoTelefono);
                statement.setString(2, nuevoNombre);
                statement.setString(3, nuevoApellido1);
                statement.setString(4, nuevoApellido2);
                statement.setString(5, nuevaDireccion);
                statement.setString(6, nuevaFechaNac);
                statement.setString(7, nuevoEmail);
                statement.setString(8, nuevaCategoria);
                statement.setBytes(9, fotoBytes);
                statement.setString(10, telefonoSeleccionado);
                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                conexion.cerrarConexion();
            }

            JOptionPane.showMessageDialog(null, "El/La tripulante se ha editado correctamente");

            // Actualizar la lista de miembros
            miembrosModel.clear();
            ConexionBD conexionBD2 = new ConexionBD();
            ResultSet salida = conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
            try {
                while (salida.next()) {
                    String telefonoDB = salida.getString("telefono");
                    String nombreDB = salida.getString("nombre");
                    String apellido1DB = salida.getString("apellido1");
                    String apellido2DB = salida.getString("apellido2");
                    String categoriaDB = salida.getString("categoria");
                    String miembro = telefonoDB + " " + nombreDB + " " + apellido1DB + " " + apellido2DB + " (" + categoriaDB + ")";
                    miembrosModel.addElement(miembro);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            } finally {
                conexionBD2.cerrarConexion();
            }

            Logomens log = new Logomens();
            log.escribirRegistro("Tripulante "+telefonoSeleccionado+" editado");
        }
    }

    private byte[] obtenerFotoTripulante(String telefono) {
        ConexionBD conexionBD = new ConexionBD();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        byte[] fotoBytes = null; // Inicializar con valor nulo

        try {
            // Consulta SQL para obtener la foto del tripulante
            String query = "SELECT foto FROM miembros WHERE telefono = ?";
            statement = conexionBD.getConexion().prepareStatement(query);
            statement.setString(1, telefono);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtener la foto del resultado de la consulta
                fotoBytes = resultSet.getBytes("foto");
            }

            // Si no hay foto disponible en la base de datos, asignar una foto por defecto
            if (fotoBytes == null) {
                try {
                    inputStream = imgUrl.openStream();
                    fotoBytes = leerBytesFoto(inputStream);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        } finally {
            conexionBD.cerrarConexion();
        }
        return fotoBytes;
    }
    // Método para leer los bytes de una imagen
    private byte[] leerBytesFoto(File file) throws IOException {
        byte[] resizedBytes = EditarPasajero.leerBytesFoto(file);

        return resizedBytes;
    }
    private byte[] leerBytesFoto(InputStream fis) throws IOException {
        byte[] resizedBytes = EditarPasajero.leerBytesFoto(fis);

        return resizedBytes;
    }

}