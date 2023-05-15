package vista;

import controlador.Logomens;
import modelo.ConexionBD;
import com.toedter.calendar.JCalendar;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditarTripulante {
    public EditarTripulante(){
    }
    public void actionPerformed(ActionEvent e, String selected, DefaultListModel<String> miembrosModel) {
        PestañaTripulantes pesTrip = new PestañaTripulantes();
        // Obtener el miembro seleccionado de la lista
        String miembroSeleccionado = selected;

        // Extraer el número de teléfono del miembro seleccionado
        String telefonoSeleccionado = miembroSeleccionado.substring(0, miembroSeleccionado.indexOf(" "));

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
            throw new RuntimeException(ex);
        }
        conexionBD.cerrarConexion();

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
        JFileChooser      fileChooser       = new JFileChooser();
        FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png");
        fileChooser.addChoosableFileFilter(imgFilter);
        ActionListener fotoChooser = new ActionListener() {
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
        };
        selecFoto.addActionListener(fotoChooser);
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
        int option = JOptionPane.showConfirmDialog(null, message, "Editar Miembro", JOptionPane.OK_CANCEL_OPTION);
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
            byte[] fotoBytes = null;
            try {
                fotoBytes = leerBytesFoto(fotoFile);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            // Actualizar los datos del miembro en la base de datos, incluyendo la foto
            ConexionBD conexion = new ConexionBD();
            PreparedStatement statement;
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
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(null, "El/La tripulante se ha editado correctamente");
            conexion.cerrarConexion();

            // Actualizar la lista de miembros
            miembrosModel.clear();
            ConexionBD conexionBD2 = new ConexionBD();
            ResultSet salida = conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
            while (true) {
                try {
                    if (!salida.next()) break;
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String telefonoDB = null;
                try {
                    telefonoDB = salida.getString("telefono");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String nombreDB = null;
                try {
                    nombreDB = salida.getString("nombre");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String apellido1DB = null;
                try {
                    apellido1DB = salida.getString("apellido1");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String apellido2DB = null;
                try {
                    apellido2DB = salida.getString("apellido2");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String categoriaDB = null;
                try {
                    categoriaDB = salida.getString("categoria");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String miembro = telefonoDB + " " + nombreDB + " " + apellido1DB + " " + apellido2DB + " (" + categoriaDB + ")";
                miembrosModel.addElement(miembro);
            }
            Logomens log = new Logomens();
            log.escribirRegistro("Tripulante editado correctamente.");
            conexionBD2.cerrarConexion();
        }
    }

    // Método para leer los bytes de una imagen
    private byte[] leerBytesFoto(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1; ) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            throw ex;
        }
        byte[] bytes = bos.toByteArray();
        fis.close();
        return bytes;
    }
}