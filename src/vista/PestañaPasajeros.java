package vista;

import javax.swing.*;
import java.awt.*;

public class PestañaPasajeros {
    public void pestañaPasajeros (JTabbedPane tabbedPane ) {
        // Pestaña de Pasajeros
        JPanel passengerPanel = new JPanel ( );
        passengerPanel.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de pasajeros
        JPanel tablePanel = new JPanel ( );
        tablePanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        tablePanel.setLayout ( new BorderLayout ( ) );

        // Tabla de pasajeros
        String[] columnNames = { "idPasajero" ,"nombre" ,"apellido1" ,"apellido2" ,"edad" ,"telefono" ,"ecorreo" ,"direccion" ,"foto" };
        Object[][] data = { { 1 ,"Juan" ,"Perez" ,"Gomez" ,30 ,"123456789" ,"juan.perez@example.com" ,"Calle 123, Ciudad" ,"foto1.jpg" } ,{ 2 ,"Maria" ,"Lopez" ,"Gonzalez" ,25 ,"987654321" ,"maria.lopez@example.com" ,"Avenida 456, Pueblo" ,"foto2.jpg" } ,{ 3 ,"Pedro" ,"Gomez" ,"Rodriguez" ,40 ,"456789123" ,"pedro.gomez@example.com" ,"Carretera 789, Villa" ,"foto3.jpg" } };
        JTable passengerTable = new JTable ( data ,columnNames );
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
