package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AñadirPasajero extends JFrame {
    AñadirPasajero(JTable flightsTable){
        this.setLayout(new BorderLayout());
        JLabel vueloLabel = new JLabel("Pasajeros");
        JPanel vueloPanel = new JPanel();
        vueloPanel.add(vueloLabel);
        this.add(vueloPanel, BorderLayout.NORTH);
        JPanel datosPasajeros = new JPanel();
        datosPasajeros.setLayout(new GridLayout(6,2));

        JLabel idPasajeroLabel = new JLabel("ID Pasajero: ");
        datosPasajeros.add(idPasajeroLabel);
        JTextField idpasajeroTexfield = new JTextField();
        datosPasajeros.add(idpasajeroTexfield);

        JLabel DNILabel = new JLabel("DNI: ");
        datosPasajeros.add(DNILabel);
        JTextField DNITexfield = new JTextField();
        datosPasajeros.add(DNITexfield
        );
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
        String query = "SELECT idAvion from aviones where estado=1 order by 1";
        ResultSet resul = con.ejecutarConsulta(query);
        while(true){
            try {
                if (!resul.next()) break;
                idAvionesCombo.addItem(String.valueOf(resul.getInt("idAvion")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        datosPasajeros.add(idAvionesCombo);

        JButton aceptar = new JButton("ACEPTAR");
        JButton limpiar = new JButton("LIMPIAR CAMPOS");
        JPanel botones = new JPanel();

        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores de los campos de texto y el combo box
                int idPasajero = Integer.parseInt(idpasajeroTexfield.getText());
                String dni = DNITexfield.getText();
                String nombre = nombreTexfield.getText();
                String apellido1 = aapellido1Texfield.getText();
                String apellido2 = apellido2Texfield.getText();
                String edad = edadTexfield.getText();
                String telefono = telefonoTexfield.getText();
                String correo = correoTexfield.getText();
                String direccion = direccionTexfield.getText();


                // Guardar los datos en la base de datos
                ConexionBD con = new ConexionBD();
                try {
                    PreparedStatement p = con.getConexion().prepareStatement("""
                INSERT INTO pasajeros(idPasajeros, DNI, Nombre, Apellido1, Apellido2, fechaNacimiento, Telefono, ecorreo, Direccion)
                VALUES (?,?,?,?,?,?,?,?,?);
            """);
                    p.setInt(1, idPasajero);
                    p.setString(2, dni);
                    p.setString(3, nombre);
                    p.setString(4, apellido1);
                    p.setString(5, apellido2);
                    p.setString(6, edad);
                    p.setString(7, telefono);
                    p.setString(8, correo);
                    p.setString(9, direccion);
                    p.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
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

            }
        });
        botones.add(aceptar);
        botones.add(limpiar);
        this.add(botones,BorderLayout.SOUTH);
        this.add(datosPasajeros,BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
    }


}
