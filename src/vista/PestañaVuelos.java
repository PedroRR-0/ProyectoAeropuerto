package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PestañaVuelos {
    public void pestañaVuelos(JTabbedPane tabbedPane){
        // Pestaña de Vuelos
        JPanel flightsPanel = new JPanel ( );
        flightsPanel.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de vuelos
        JPanel tablaVuelos = new JPanel ( );
        tablaVuelos.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        tablaVuelos.setLayout ( new BorderLayout ( ) );

        // Tabla de vuelos
        String[] NombreCOlumnas = { "idVuelo" , "IdAvión" , "origen" , "Destino" , "Hora de Salida" , "Hora de llegada" };
        Object[][] datos = {
                { 1 , "Juan" , "Perez" , "Gomez" , 30 , "123456789" , "juan.perez@example.com" , "Calle 123, Ciudad" , "foto1.jpg" } ,
                { 2 , "Maria" , "Lopez" , "Gonzalez" , 25 , "987654321" , "maria.lopez@example.com" , "Avenida 456, Pueblo" , "foto2.jpg" } ,
                { 3 , "Pedro" , "Gomez" , "Rodriguez" , 40 , "456789123" , "pedro.gomez@example.com" , "Carretera 789, Villa" , "foto3.jpg" }
        };
        DefaultTableModel model             = new DefaultTableModel ( datos , NombreCOlumnas );
        JTable            flightsTable      = new JTable ( model );
        JScrollPane       flightsScrollPane = new JScrollPane ( flightsTable );
        tablaVuelos.add ( flightsScrollPane , BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de vuelos
        flightsPanel.add ( tablaVuelos , BorderLayout.CENTER );

        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addFlightButton     = new JButton ( "Agregar" );
        JButton editFlightButton    = new JButton ( "Editar" );
        JButton deleteFlightButton  = new JButton ( "Eliminar" );
        JPanel  flightsButtonsPanel = new JPanel ( );
        flightsButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        flightsButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER , 20 , 10 ) ); // Mayor margen entre los botones
        flightsButtonsPanel.add ( addFlightButton );
        flightsButtonsPanel.add ( editFlightButton );
        flightsButtonsPanel.add ( deleteFlightButton );
        flightsPanel.add ( flightsButtonsPanel , BorderLayout.SOUTH );

        // Agregar el panel de vuelos al panel de pestañas
        tabbedPane.addTab ( "Vuelos" , flightsPanel );
    }
}
