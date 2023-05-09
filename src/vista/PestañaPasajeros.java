package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestañaPasajeros {
    public void pestañaPasajeros (JTabbedPane tabbedPane ) throws SQLException {
        // Pestaña de Pasajeros
        JPanel passengerPanel = new JPanel ( );
        passengerPanel.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de pasajeros
        JPanel tablePanel = new JPanel ( );
        tablePanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        tablePanel.setLayout ( new BorderLayout ( ) );

        // Tabla de pasajeros
        String[] columnNames = { "idPasajero" , "DNI", "nombre" ,"apellido1" ,"apellido2" ,
                "edad" ,"telefono" ,"Correo electrónico" ,"direccion" , "ID Vuelo"};
        ConexionBD conexionBD = new ConexionBD ( );
        // Ejecutar una consulta para obtener todos los aviones de la tabla de aviones
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM pasajeros");
        ResultSet resultado3 = conexionBD.ejecutarConsulta("SELECT edad(fechaNacimiento) as e FROM pasajeros;");
        DefaultTableModel contenidoTablaPasajeros = new DefaultTableModel ( new Object[][]{}, columnNames );
        boolean flag;
        while (resultado.next()){
            flag = false;
            resultado3.next();
            ResultSet resultado2 = conexionBD.ejecutarConsulta ( "SELECT * FROM pasajeros_vuelos where idPasajeros="+resultado.getInt("idPasajeros"));
            while(resultado2.next()) {
                    contenidoTablaPasajeros.addRow(new Object[]{
                            resultado.getInt("idPasajeros"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado3.getString("e"),
                            resultado.getString("telefono"),
                            resultado.getString("ecorreo"),
                            resultado.getString("direccion"),
                            resultado2.getInt("idVuelo")

                });
                flag = true;
            }
                if (!flag) {
                    contenidoTablaPasajeros.addRow(new Object[]{
                            resultado.getInt("idPasajeros"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado3.getString("e"),
                            resultado.getString("telefono"),
                            resultado.getString("ecorreo"),
                            resultado.getString("direccion")
                    });
                }
        }
        JTable passengerTable = new JTable (contenidoTablaPasajeros);
        passengerTable.setDefaultEditor(Object.class, null);
        JScrollPane passengerScrollPane = new JScrollPane ( passengerTable );
        tablePanel.add ( passengerScrollPane ,BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de pasajeros
        passengerPanel.add ( tablePanel ,BorderLayout.CENTER );

        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addPassengerButton = new JButton ( "Agregar" );
        JButton editPassengerButton = new JButton ( "Editar" );
        JButton deletePassengerButton = new JButton ( "Eliminar" );
        JPanel passengerButtonsPanel = new JPanel ( );
        passengerButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        passengerButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER ,20 ,10 ) ); // Mayor margen entre los botones
        passengerButtonsPanel.add ( addPassengerButton );
        passengerButtonsPanel.add ( editPassengerButton );
        passengerButtonsPanel.add ( deletePassengerButton );
        passengerPanel.add ( passengerButtonsPanel ,BorderLayout.SOUTH );

        // Agregar el panel de pasajeros al panel de pestañas
        tabbedPane.addTab ( "Pasajeros" ,passengerPanel );
    }
}
