package vista;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.sql.SQLException;

public
class Pestanias extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel      planesPanel, crewPanel, passengersPanel, flightsPanel, assignmentsPanel;

    public
    Pestanias ( ) throws SQLException {
        // Configuración de la ventana principal
        setTitle ( "Gestión de Vuelos" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setSize ( 1000 , 700 );
        setLocationRelativeTo ( null );

        // Creación de las pestañas
        tabbedPane = new JTabbedPane ( );

        // Llamada para la creación de la pestaña aviones
        PestañaAviones pestañaAviones = new PestañaAviones();
        pestañaAviones.PestañaAviones(planesPanel,tabbedPane);

        add(tabbedPane);
        // Mostrar la ventana principal
        setVisible ( true );

        // Llamada para la creación de la pestaña tripulantes
        PestañaTripulantes pestañaTripulantes = new PestañaTripulantes();
        crewPanel = new JPanel();
        pestañaTripulantes.pestañaTripulantes(crewPanel,tabbedPane);

        // Llamada para la creación de la pestaña pasajeros
        PestañaPasajeros pestañaPasajeros = new PestañaPasajeros();
        pestañaPasajeros.pestañaPasajeros(tabbedPane);

        // Llamada para la creación de la pestaña vuelos
        PestaniaVuelos pestañaVuelos = new PestaniaVuelos ();
        tabbedPane.add("Vuelos",pestañaVuelos);

        // Llamada a la pestaña Asignación
        PestañaAsignacion pestañaAsignacion = new PestañaAsignacion (tabbedPane);

        // Agregar las pestañas a la ventana
        add ( tabbedPane );
        tabbedPane.addChangeListener(new ChangeListener () {
            @Override
            public void stateChanged( ChangeEvent e) {
                int selectedIndex = tabbedPane.getSelectedIndex();
                if (selectedIndex == tabbedPane.indexOfTab("Asignación")) {
                    // Si se selecciona la pestaña "Asignación", llama al método para actualizar la lista de tripulantes
                    pestañaAsignacion.actualizarListaTripulantes();
                }
            }
        });
        // Mostrar la ventana principal
        setVisible ( true );
    }
}