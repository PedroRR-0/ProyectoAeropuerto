package vista.emergentesPasajero;

import modelo.ConexionBD;
import vista.PestañaPasajeros;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EditarPasajero extends JFrame {
    private String idVueloSeleccionado;
    public EditarPasajero(String selec, JTable flightsTable) throws SQLException {
        this.setLayout(new BorderLayout());
        JLabel vueloLabel = new JLabel("Pasajeros");
        JPanel vueloPanel = new JPanel();
        vueloPanel.add(vueloLabel);
        this.add(vueloPanel, BorderLayout.NORTH);
        JPanel datosPasajeros = new JPanel();
        datosPasajeros.setLayout(new GridLayout(6, 2));

        JLabel idPasajeroLabel = new JLabel("ID Pasajero: ");
        datosPasajeros.add(idPasajeroLabel);
        JTextField idpasajeroTexfield = new JTextField();
        datosPasajeros.add(idpasajeroTexfield);

        JLabel DNILabel = new JLabel("DNI: ");
        datosPasajeros.add(DNILabel);
        JTextField DNITexfield = new JTextField();
        datosPasajeros.add(DNITexfield);

        JLabel nombreLabel = new JLabel("Nombre: ");
        datosPasajeros.add(nombreLabel);
        JTextField nombreTexfield = new JTextField();
        datosPasajeros.add(nombreTexfield);

        JLabel apellido1Label = new JLabel("Apellido1: ");
        datosPasajeros.add(apellido1Label);
        JTextField aapellido1Texfield = new JTextField();
        datosPasajeros.add(aapellido1Texfield);

        JLabel apellido2Label = new JLabel("Apellido2:  ");
        datosPasajeros.add(apellido2Label);
        JTextField apellido2Texfield = new JTextField();
        datosPasajeros.add(apellido2Texfield);

        JLabel edadLabel = new JLabel("Fecha Nacimiento: ");
        datosPasajeros.add(edadLabel);
        JTextField edadTexfield = new JTextField();
        datosPasajeros.add(edadTexfield);

        JLabel telefonoLabel = new JLabel("Teléfono: ");
        datosPasajeros.add(telefonoLabel);
        JTextField telefonoTexfield = new JTextField();
        datosPasajeros.add(telefonoTexfield);

        JLabel correoLabel = new JLabel("Correo Electrónico: ");
        datosPasajeros.add(correoLabel);
        JTextField correoTexfield = new JTextField();
        datosPasajeros.add(correoTexfield);

        JLabel direccionLabel = new JLabel("Dirección: ");
        datosPasajeros.add(direccionLabel);
        JTextField direccionTexfield = new JTextField();
        datosPasajeros.add(direccionTexfield);

        JLabel idVueloLabel = new JLabel("idVuelo: ");
        datosPasajeros.add(idVueloLabel);
        JComboBox<String> idAvionesCombo = new JComboBox<>();
        ConexionBD con = new ConexionBD();
        String query = "SELECT distinct(idVuelo) from vuelos";
        ResultSet resul = con.ejecutarConsulta(query);

        while (resul.next()) {
            String idAvion = String.valueOf(resul.getInt("idVuelo"));
            idAvionesCombo.addItem(idAvion);

            if (idAvion.equals(idVueloSeleccionado)) {
                idAvionesCombo.setSelectedItem(idAvion);
            }
        }
        datosPasajeros.add(idAvionesCombo);

        JButton aceptar = new JButton("ACEPTAR");
        JButton limpiar = new JButton("LIMPIAR CAMPOS");
        JPanel botones = new JPanel();

        // Obtener los datos del pasajero seleccionado

        PreparedStatement stmt = con.getConexion().prepareStatement("SELECT * FROM pasajeros WHERE idPasajeros = ?");
        stmt.setString(1, selec);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()){
            // Obtener los valores de las columnas de la consulta
            String idPasajero = rs.getString ( "idPasajeros" );
            String dni = rs.getString ( "DNI" );
            String nombre = rs.getString ( "nombre" );
            String apellido1 = rs.getString ( "apellido1" );
            String apellido2 = rs.getString ( "apellido2" );
            String edad = rs.getString ( "fechaNacimiento" );
            String telefono = rs.getString ( "telefono" );
            String correo = rs.getString ( "ecorreo" );
            String direccion = rs.getString ( "direccion" );

            // Establecer los valores en los campos de texto
            idpasajeroTexfield.setText ( idPasajero );
            DNITexfield.setText ( dni );
            nombreTexfield.setText ( nombre );
            aapellido1Texfield.setText ( apellido1 );
            apellido2Texfield.setText ( apellido2 );
            edadTexfield.setText ( edad );
            telefonoTexfield.setText ( telefono );
            correoTexfield.setText ( correo );
            direccionTexfield.setText ( direccion );

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


        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores de los campos de texto
                String idPasajero = idpasajeroTexfield.getText();
                String dni = DNITexfield.getText();
                String nombre = nombreTexfield.getText();
                String apellido1 = aapellido1Texfield.getText();
                String apellido2 = apellido2Texfield.getText();
                String edad = edadTexfield.getText();
                String telefono = telefonoTexfield.getText();
                String correo = correoTexfield.getText();
                String direccion = direccionTexfield.getText();
                String idVuelo = (String) idAvionesCombo.getSelectedItem();

                // Validar que los campos no estén vacíos
                if (idPasajero.isEmpty() || dni.isEmpty() || nombre.isEmpty() || apellido1.isEmpty() || apellido2.isEmpty() ||
                        edad.isEmpty() || telefono.isEmpty() || correo.isEmpty() || direccion.isEmpty() || idVuelo.isEmpty()) {
                    JOptionPane.showMessageDialog(EditarPasajero.this, "Por favor, complete todos los campos.");
                    return;
                }

                // Realizar la actualización o inserción en la base de datos
                ConexionBD con = new ConexionBD();
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

                    if (pasajeroSeleccionado) {
                        // Actualizar la tabla pasajeros
                        PreparedStatement updateStmt = con.getConexion().prepareStatement("UPDATE pasajeros SET idPasajeros = ?, DNI = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, telefono = ?, ecorreo = ?, direccion = ? WHERE idPasajeros = ?");
                        updateStmt.setString(1, idPasajero);
                        updateStmt.setString(2, dni);
                        updateStmt.setString(3, nombre);
                        updateStmt.setString(4, apellido1);
                        updateStmt.setString(5, apellido2);
                        updateStmt.setString(6, edad);
                        updateStmt.setString(7, telefono);
                        updateStmt.setString(8, correo);
                        updateStmt.setString(9, direccion);
                        updateStmt.setString(10, idPasajero);
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
                        PreparedStatement updateStmt = con.getConexion().prepareStatement("UPDATE pasajeros SET idPasajeros = ?, DNI = ?, nombre = ?, apellido1 = ?, apellido2 = ?, fechaNacimiento = ?, telefono = ?, ecorreo = ?, direccion = ? WHERE idPasajeros = ?");
                        updateStmt.setString(1, idPasajero);
                        updateStmt.setString(2, dni);
                        updateStmt.setString(3, nombre);
                        updateStmt.setString(4, apellido1);
                        updateStmt.setString(5, apellido2);
                        updateStmt.setString(6, edad);
                        updateStmt.setString(7, telefono);
                        updateStmt.setString(8, correo);
                        updateStmt.setString(9, direccion);
                        updateStmt.setString(10, idPasajero);
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
                    p.actualizarTabla(flightsTable );
                }
                catch (SQLException ex) {
                    throw new RuntimeException ( ex );
                }
                dispose();
            }
        });


        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idAvionesCombo.setSelectedItem("1");
                idpasajeroTexfield.setText("");
                nombreTexfield.setText("");
                aapellido1Texfield.setText("");
                apellido2Texfield.setText("");
                edadTexfield.setText("");
                telefonoTexfield.setText("");
                correoTexfield.setText("");
                direccionTexfield.setText("");
                DNITexfield.setText("");
                con.cerrarConexion();

            }
        });
        botones.add(aceptar);
        botones.add(limpiar);
        this.add(botones, BorderLayout.SOUTH);
        this.add(datosPasajeros, BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }
}
