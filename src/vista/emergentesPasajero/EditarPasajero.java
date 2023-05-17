package vista.emergentesPasajero;

import com.toedter.calendar.JCalendar;
import controlador.Logomens;
import modelo.ConexionBD;
import vista.PestañaPasajeros;

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
import java.text.SimpleDateFormat;

public class EditarPasajero {
    private String idVueloSeleccionado;
    JTextField idPasajeroField;
    JTextField dniField;
    JTextField nombreField;
    JTextField apellido1Field;
    JTextField apellido2Field;
    JCalendar fechaNacimientoCalendar;
    JTextField telefonoField;
    JTextField correoField;
    JTextField direccionField;
    String dni;
    URL imgUrl = getClass().getResource("/default.png");
    InputStream inputStream = imgUrl.openStream();
    public EditarPasajero() throws IOException {}


    public void actionPerformed(ActionEvent e, String selected, JTable vuelosTab) throws IOException, SQLException {
        // Obtener el miembro seleccionado de la lista

        // Extraer el número de teléfono del miembro seleccionado
        JComboBox<String> idAvionesCombo = new JComboBox<>();
        // Obtener los datos actuales del miembro desde la base de datos
        ConexionBD con = new ConexionBD();
        String query = "SELECT distinct(idVuelo) from vuelos";
        ResultSet resul = con.ejecutarConsulta(query);

        while (true) {
            try {
                if (!resul.next()) break;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String idAvion = String.valueOf(resul.getInt("idVuelo"));
            idAvionesCombo.addItem(idAvion);

            if (idAvion.equals(idVueloSeleccionado)) {
                idAvionesCombo.setSelectedItem(idAvion);
            }
        }
        PreparedStatement stmt = con.getConexion().prepareStatement("SELECT * FROM pasajeros WHERE idPasajeros = ?");
        stmt.setString(1, selected);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            // Obtener los valores de las columnas de la consulta
            String idPasajero = rs.getString ( "idPasajeros" );
            dni = rs.getString ( "DNI" );
            String nombre = rs.getString ( "nombre" );
            String apellido1 = rs.getString ( "apellido1" );
            String apellido2 = rs.getString ( "apellido2" );
            String fechaNac = rs.getString ( "fechaNacimiento" );
            String telefono = rs.getString ( "telefono" );
            String correo = rs.getString ( "ecorreo" );
            String direccion = rs.getString ( "direccion" );

            // Establecer los valores en los campos de texto
            idPasajeroField = new JTextField(idPasajero);
            dniField = new JTextField(dni);
            nombreField = new JTextField(nombre);
            apellido1Field = new JTextField(apellido1);
            apellido2Field = new JTextField(apellido2);
            fechaNacimientoCalendar = new JCalendar();
            fechaNacimientoCalendar.setDate(java.sql.Date.valueOf(fechaNac));
            telefonoField = new JTextField(telefono);
            correoField = new JTextField(correo);
            direccionField = new JTextField(direccion);

            // Obtener el idVuelo del pasajero seleccionado desde la tabla pasajeros_vuelos
            String query3 = "SELECT idVuelo FROM pasajeros_vuelos WHERE idPasajeros = ?";
            PreparedStatement stmt2 = con.getConexion().prepareStatement(query3);
            stmt2.setString(1, idPasajero);
            ResultSet rs2 = stmt2.executeQuery();

            if (rs2.next()) {
                idVueloSeleccionado = rs2.getString("idVuelo"); // Asignar el valor a la variable idVueloSeleccionado
            } else {
                // Establecer un valor predeterminado en caso de que no haya un idVuelo asociado al pasajero seleccionado
                idVueloSeleccionado = "No asignado";
            }
            // Establecer el valor seleccionado en el JComboBox
            if (idVueloSeleccionado != null) {
                idAvionesCombo.setSelectedItem(idVueloSeleccionado);
            } else {
                idAvionesCombo.setSelectedIndex(0); // Establece el valor predeterminado como seleccionado
            }
            rs2.close();
            stmt2.close();
        }
        rs.close();
        stmt.close();
        // Mostrar una ventana de diálogo con los datos actuales del miembro para editarlos

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
                "ID Pasajero: ", idPasajeroField,
                "Nombre:", nombreField,
                "Primer Apellido:", apellido1Field,
                "Segundo Apellido:", apellido2Field,
                "Teléfono:", telefonoField,
                "Dirección:", direccionField,
                "Fecha de Nacimiento:", fechaNacimientoCalendar,
                "Email:", correoField,
                "DNI: ", dniField,
                "Foto:", selecFoto,
                "ID Vuelo: ",idAvionesCombo
        };
        //Crear un ImagenIcon personalizado
        ImageIcon cambio = new ImageIcon ( obtenerFotoTripulante(dni));

        int option = JOptionPane.showConfirmDialog(null, message, "Editar Pasajero", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, cambio);
        if (option == JOptionPane.OK_OPTION) {

            // Obtener los valores de los campos de texto
            String idPasajero = idPasajeroField.getText();
            String dni = dniField.getText();
            String nombre = nombreField.getText();
            String apellido1 = apellido1Field.getText();
            String apellido2 = apellido2Field.getText();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy/MM/dd");
            String edad = formato.format(fechaNacimientoCalendar.getDate());
            String telefono = telefonoField.getText();
            String correo = correoField.getText();
            String direccion = direccionField.getText();
            String idVuelo = (String) idAvionesCombo.getSelectedItem();

            // Validar que los campos no estén vacíos
            if (idPasajero.isEmpty() || dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || apellido2.isEmpty() ||
                    edad.isEmpty() || telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty() || idVuelo.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, complete todos los campos.");
                return;
            }

            // Realizar la actualización o inserción en la base de datos
            con = new ConexionBD();
            try {
                // Comprobar si el pasajero tiene algún vuelo seleccionado en la tabla pasajeros_vuelos
                PreparedStatement checkStmt = con.getConexion().prepareStatement("SELECT idVuelo FROM pasajeros_vuelos WHERE idPasajeros = ? and idVuelo= ?");
                checkStmt.setString(1, idPasajero);
                checkStmt.setString ( 2,idVuelo );
                ResultSet checkResult = checkStmt.executeQuery();

                boolean pasajeroSeleccionado = checkResult.next();
                // Realizar la consulta para obtener el idAvion desde la tabla vuelos
                PreparedStatement selectVueloStmt = con.getConexion().prepareStatement("SELECT idAvion FROM vuelos WHERE idVuelo = ?");
                selectVueloStmt.setString(1, idVuelo);
                ResultSet vueloResult = selectVueloStmt.executeQuery();
                String idAvion = null;
                if (vueloResult.next()) {
                    idAvion = vueloResult.getString("idAvion");
                }
                checkResult.close();
                checkStmt.close();
                File fotoFile = fileChooser.getSelectedFile();

                byte[] fotoBytes = obtenerFotoTripulante (dni);
                if (fotoFile != null) {
                    fotoBytes = leerBytesFoto(fotoFile);
                }

                if (pasajeroSeleccionado) {
                    // Actualizar la tabla pasajeros
                    PreparedStatement updateStmt = con.getConexion().prepareStatement("UPDATE pasajeros SET idPasajeros = ?, DNI = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, telefono = ?, ecorreo = ?, direccion = ?, foto=? WHERE idPasajeros = ?");
                    updateStmt.setString(1, idPasajero);
                    updateStmt.setString(2, dni);
                    updateStmt.setString(3, nombre);
                    updateStmt.setString(4, apellido1);
                    updateStmt.setString(5, apellido2);
                    updateStmt.setString(6, edad);
                    updateStmt.setString(7, telefono);
                    updateStmt.setString(8, correo);
                    updateStmt.setString(9, direccion);
                    updateStmt.setBytes(10,fotoBytes);
                    updateStmt.setString(11, idPasajero);
                    updateStmt.executeUpdate();
                    updateStmt.close();

                    // Actualizar la tabla pasajeros_vuelos
                    PreparedStatement updateVueloStmt = con.getConexion().prepareStatement("UPDATE pasajeros_vuelos SET idVuelo = ? WHERE idPasajeros = ?");
                    updateVueloStmt.setString(1, idVuelo);
                    updateVueloStmt.setString(2, idPasajero);
                    updateVueloStmt.executeUpdate();
                    updateVueloStmt.close();
                } else {
                    // Actualizar la tabla pasajeros
                    PreparedStatement updateStmt = con.getConexion().prepareStatement("UPDATE pasajeros SET idPasajeros = ?, DNI = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, telefono = ?, ecorreo = ?, direccion = ?, foto=? WHERE idPasajeros = ?");
                    updateStmt.setString(1, idPasajero);
                    updateStmt.setString(2, dni);
                    updateStmt.setString(3, nombre);
                    updateStmt.setString(4, apellido1);
                    updateStmt.setString(5, apellido2);
                    updateStmt.setString(6, edad);
                    updateStmt.setString(7, telefono);
                    updateStmt.setString(8, correo);
                    updateStmt.setString(9, direccion);
                    updateStmt.setBytes(10,fotoBytes);
                    updateStmt.setString(11, idPasajero);

                    updateStmt.executeUpdate();
                    updateStmt.close();

                    // Insertar un nuevo registro en la tabla pasajeros_vuelos
                    PreparedStatement insertStmt = con.getConexion().prepareStatement("INSERT INTO pasajeros_vuelos (idVuelo, idAvion, idPasajeros) VALUES (?, ?, ?)");
                    insertStmt.setString(1, idVuelo);
                    insertStmt.setString ( 2, idAvion );
                    insertStmt.setString(3, idPasajero);
                    insertStmt.executeUpdate();
                    insertStmt.close();
                }
            } catch (SQLException ex) {
                // Manejar la excepción
                ex.printStackTrace();
            } finally {
                // Cerrar las conexiones
                con.cerrarConexion();
            }
            // Actualizar la tabla de vuelos
            PestañaPasajeros p = null;
            p = new PestañaPasajeros ();
            try {
                p.actualizarTabla(vuelosTab );
            }
            catch (SQLException ex) {
                throw new RuntimeException ( ex );
            }
            Logomens log = new Logomens();
            log.escribirRegistro("Pasajero "+idPasajero+" editado ");
        }
    }

    private byte[] obtenerFotoTripulante(String dni) {
        ConexionBD conexionBD = new ConexionBD();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        byte[] fotoBytes = null; // Inicializar con valor nulo

        try {
            // Consulta SQL para obtener la foto del tripulante
            String query = "SELECT foto FROM pasajeros WHERE dni = ?";
            statement = conexionBD.getConexion().prepareStatement(query);
            statement.setString(1, dni);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Obtener la foto del resultado de la consulta
                fotoBytes = resultSet.getBytes("foto");
            }

            // Si no hay foto disponible en la base de datos, asignar una foto por defecto
            if (fotoBytes == null) {
                try {
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
    public static byte[] leerBytesFoto(File file) throws IOException {
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
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytes));
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
    public static byte[] leerBytesFoto(InputStream fis) throws IOException {
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
        BufferedImage originalImage = ImageIO.read(new ByteArrayInputStream(bytes));
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
