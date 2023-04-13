import javax.swing.*;
import java.awt.*;

public class Pestanias extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel      planesPanel, crewPanel, passengersPanel, flightsPanel, assignmentsPanel;

    public Pestanias ( ) {
        // Configuración de la ventana principal
        setTitle ( "Gestión de Vuelos" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setSize ( 800 , 600 );
        setLocationRelativeTo ( null );

        // Creación de las pestañas
        tabbedPane = new JTabbedPane ( );

        // Pestaña de Aviones
        planesPanel = new JPanel ( );
        planesPanel.setLayout ( new BorderLayout ( ) );
        JTable      planesTable      = new JTable ( );
        JScrollPane planesScrollPane = new JScrollPane ( planesTable );
        planesPanel.add ( planesScrollPane , BorderLayout.CENTER );
        tabbedPane.addTab ( "Aviones" , planesPanel );

        // Pestaña de Tripulantes
        crewPanel = new JPanel ( );
        crewPanel.setLayout ( new BorderLayout ( ) );
        JTable      crewTable      = new JTable ( );
        JScrollPane crewScrollPane = new JScrollPane ( crewTable );
        crewPanel.add ( crewScrollPane , BorderLayout.CENTER );
        JButton viewCrewButton = new JButton ( "Ver" );
        crewPanel.add ( viewCrewButton , BorderLayout.SOUTH );
        tabbedPane.addTab ( "Tripulantes" , crewPanel );

        // Pestaña de Pasajeros
        passengersPanel = new JPanel ( );
        passengersPanel.setLayout ( new BorderLayout ( ) );
        JTable      passengersTable      = new JTable ( );
        JScrollPane passengersScrollPane = new JScrollPane ( passengersTable );
        passengersPanel.add ( passengersScrollPane , BorderLayout.CENTER );
        JButton addPassengerButton     = new JButton ( "Agregar" );
        JButton editPassengerButton    = new JButton ( "Editar" );
        JButton deletePassengerButton  = new JButton ( "Eliminar" );
        JPanel  passengersButtonsPanel = new JPanel ( );
        passengersButtonsPanel.add ( addPassengerButton );
        passengersButtonsPanel.add ( editPassengerButton );
        passengersButtonsPanel.add ( deletePassengerButton );
        passengersPanel.add ( passengersButtonsPanel , BorderLayout.SOUTH );
        tabbedPane.addTab ( "Pasajeros" , passengersPanel );

        // Pestaña de Vuelos
        flightsPanel = new JPanel ( );
        flightsPanel.setLayout ( new BorderLayout ( ) );
        JTable      flightsTable      = new JTable ( );
        JScrollPane flightsScrollPane = new JScrollPane ( flightsTable );
        flightsPanel.add ( flightsScrollPane , BorderLayout.CENTER );
        tabbedPane.addTab ( "Vuelos" , flightsPanel );

        // Pestaña de Asignación
        assignmentsPanel = new JPanel ( );
        assignmentsPanel.setLayout ( new BorderLayout ( ) );
        JTable      assignmentsTable      = new JTable ( );
        JScrollPane assignmentsScrollPane = new JScrollPane ( assignmentsTable );
        assignmentsPanel.add ( assignmentsScrollPane , BorderLayout.CENTER );
        tabbedPane.addTab ( "Asignación" , assignmentsPanel );

        // Agregar las pestañas a la ventana
        add ( tabbedPane );

        // Mostrar la ventana principal
        setVisible ( true );
    }
}