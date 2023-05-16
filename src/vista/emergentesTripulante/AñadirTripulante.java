package vista.emergentesTripulante;

import controlador.Logomens;
import modelo.ConexionBD;
import com.toedter.calendar.JCalendar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AñadirTripulante {
    private static final String DEFAULT_IMAGE_PATH = "Recursos/default.png";

    public AñadirTripulante() {
    }

    public void actionPerformed(ActionEvent e, DefaultListModel<String> miembrosModel) throws IOException {
        // Mostrar una ventana de diálogo para ingresar los datos del nuevo miembro
        JTextField telefonoField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellido1Field = new JTextField();
        JTextField apellido2Field = new JTextField();
        JTextField direccionField = new JTextField();
        JCalendar fechaNacimientoCalendar = new JCalendar();
        JTextField emailField = new JTextField();
        String[] categorias = {"Piloto", "Copiloto", "Ingeniero de vuelo", "Azafato"};
        JComboBox<String> categoriaComboBox = new JComboBox<>(categorias);
        JButton selecFoto = new JButton("Seleccionar");
        JFileChooser fileChooser = new JFileChooser();
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
                    JOptionPane.showMessageDialog(null, "No se seleccionó ninguna foto");
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
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        int option = JOptionPane.showConfirmDialog(null, message, "Agregar Nuevo Miembro", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            // Obtener los datos ingresados por el usuario
            String telefono = telefonoField.getText();
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            String direccion = direccionField.getText();
            String fechanac = String.format("%tF", fechaNacimientoCalendar.getCalendar());
            String email = emailField.getText();
            String categoria = (String) categoriaComboBox.getSelectedItem();
            // Obtener la ruta de la imagen seleccionada
            // Obtener el archivo seleccionado
            File selectedFile = fileChooser.getSelectedFile();

            String imagePath = null;
            if (selectedFile != null) {
                imagePath = selectedFile.getAbsolutePath();
            }

            // Leer la foto seleccionada por el usuario y convertirla en un arreglo de bytes
            byte[] fotoBytes = null;
            if (imagePath != null) {
                fotoBytes = leerImagenBytes(selectedFile);
            } else {
                fotoBytes = leerImagenBytes( new File ( DEFAULT_IMAGE_PATH ) );
            }

            // Insertar el nuevo miembro en la base de datos con la imagen
            ConexionBD conexionBD = new ConexionBD();
            PreparedStatement statement = null;
            try {
                statement = conexionBD.getConexion().prepareStatement("INSERT INTO miembros (telefono, nombre, apellido1, apellido2, direccion, fechaNacimiento, ecorreo, categoria, foto) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, telefono);
                statement.setString(2, nombre);
                statement.setString(3, apellido1);
                statement.setString(4, apellido2);
                statement.setString(5, direccion);
                statement.setString(6, fechanac);
                statement.setString(7, email);
                statement.setString(8, categoria);
                statement.setBytes(9, fotoBytes);
                statement.executeUpdate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                conexionBD.cerrarConexion();
            }

            // Actualizar la lista de miembros
            miembrosModel.clear();
            ConexionBD conexionBD2 = new ConexionBD();
            ResultSet salida = conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
            while (true) {
                try {
                    if (!salida.next()) {
                        break;
                    }
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
                Logomens log = new Logomens();
                log.escribirRegistro("Tripulante añadido correctamente.");
            }
            conexionBD2.cerrarConexion();
        }
    }
    private byte[] leerImagenBytes(File file) throws IOException {
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

        // Redimensionar la imagen a 200x200 píxeles
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream (bytes));
        Image resizedImage  = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        BufferedImage resizedBufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedBufferedImage.createGraphics();
        g.drawImage(resizedImage, 0, 0, null);
        g.dispose();

        // Convertir la imagen redimensionada a un arreglo de bytes
        ByteArrayOutputStream resizedBos = new ByteArrayOutputStream();
        ImageIO.write(resizedBufferedImage, "png", resizedBos);
        byte[] resizedBytes = resizedBos.toByteArray();
        resizedBos.close();

        return resizedBytes;
    }
}