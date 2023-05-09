import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public
class Pestanias extends JFrame {

    private JTabbedPane tabbedPane;
    private JPanel      planesPanel, crewPanel, passengersPanel, flightsPanel, assignmentsPanel;

    Pestanias ( ) throws SQLException {
        // Configuración de la ventana principal
        setTitle ( "Gestión de Vuelos" );
        setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        setSize ( 800 , 600 );
        setLocationRelativeTo ( null );

        // Creación de las pestañas
        tabbedPane = new JTabbedPane ( );

        // Pestaña de Aviones
        planesPanel = new JPanel ( );
        planesPanel.setLayout ( new GridLayout ( 2 , 1 ) ); // GridLayout con dos filas y una columna
        DefaultTableModel tablaAvionesOperativos     = new DefaultTableModel ( new Object[][] { } , new String[] { "idAvion" , "matricula" , "modelo" , "numAsientos" } );
        JTable            planesTableOperativos      = new JTable ( tablaAvionesOperativos );
        JScrollPane       planesScrollPaneOperativos = new JScrollPane ( planesTableOperativos );
        planesPanel.add ( planesScrollPaneOperativos ); // Agregar la tabla de aviones operativos

        DefaultTableModel tablaAvionesNoOperativos     = new DefaultTableModel ( new Object[][] { } , new String[] { "idAvion" , "matricula" , "modelo" , "numAsientos" } );
        JTable            planesTableNoOperativos      = new JTable ( tablaAvionesNoOperativos );
        JScrollPane       planesScrollPaneNoOperativos = new JScrollPane ( planesTableNoOperativos );
        planesPanel.add ( planesScrollPaneNoOperativos ); // Agregar la tabla de aviones no operativos

        // Crear una instancia de la clase de conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD ( );

        // Ejecutar una consulta para obtener todos los aviones de la tabla de aviones
        ResultSet resultado = conexionBD.ejecutarConsulta ( "SELECT * FROM aviones" );

        // Agregar las filas de la consulta a la tabla de aviones correspondiente
        while ( resultado.next ( ) ) {
            if ( resultado.getString ( "estado" ).equals ( "1" ) ) { // Si el avión está operativo
                tablaAvionesOperativos.addRow ( new Object[] {
                        resultado.getInt ( "idAvion" ) ,
                        resultado.getString ( "matricula" ) ,
                        resultado.getString ( "modelo" ) ,
                        resultado.getInt ( "numAsientos" )
                } );
            }
            else { // Si el avión no está operativo
                tablaAvionesNoOperativos.addRow ( new Object[] {
                        resultado.getInt ( "idAvion" ) ,
                        resultado.getString ( "matricula" ) ,
                        resultado.getString ( "modelo" ) ,
                        resultado.getInt ( "numAsientos" )
                } );
            }
        }

        // Cerrar la conexión a la base de datos
        conexionBD.cerrarConexion ( );

        tabbedPane.addTab ( "Aviones" , planesPanel );

        // Agregar las pestañas a la ventana
        add ( tabbedPane );

        // Mostrar la ventana principal
        setVisible ( true );

        // Pestaña Tripulantes
        // Pestaña Tripulantes
// Crear un campo de texto para mostrar los miembros
        JList < String > miembrosList = new JList <> ( );

// Crear un modelo de lista por defecto y agregarlo a la lista
        DefaultListModel < String > miembrosModel = new DefaultListModel <> ( );
        miembrosList.setModel ( miembrosModel );

// Agregar la lista de miembros al panel de la pestaña tripulantes
        crewPanel = new JPanel ( new BorderLayout ( ) ); // Cambiar el layout a BorderLayout
        JScrollPane scrollPane = new JScrollPane ( miembrosList ); // Agregar un JScrollPane para permitir desplazamiento
        crewPanel.add ( scrollPane , BorderLayout.CENTER ); // Agregar el JScrollPane al centro

// Obtener los datos de la tabla "miembros" y agregarlos al modelo de lista
        ConexionBD conexionBD2 = new ConexionBD ( );
        ResultSet salida = conexionBD2.ejecutarConsulta ( "SELECT * FROM miembros" );
        while ( salida.next ( ) ) {
            String telefono = salida.getString ( "telefono" );
            String nombre = salida.getString ( "nombre" );
            String apellido1 = salida.getString ( "apellido1" );
            String apellido2 = salida.getString ( "apellido2" );
            String categoria = salida.getString ( "categoria" );
            String miembro = telefono + " " + nombre + " " + apellido1 + " " + apellido2 + " (" + categoria + ")";
            miembrosModel.addElement ( miembro );
        }

// Texto "Tripulación" centrado con margen superior
        JLabel titleLabel = new JLabel ( "Tripulación" );
        titleLabel.setFont ( new Font ( "Arial" , Font.BOLD , 24 ) ); // Cambiar la fuente y el tamaño
        titleLabel.setHorizontalAlignment ( JLabel.CENTER ); // Centrar el texto
        titleLabel.setBorder ( BorderFactory.createEmptyBorder ( 20 , 0 , 0 , 0 ) ); // Margen superior de 20 unidades
        crewPanel.add ( titleLabel , BorderLayout.NORTH ); // Agregar el título al norte

// Crear un panel para los detalles del tripulante
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Agregar un borde

// Crear los JLabels con la información del tripulante
        JLabel nameLabel = new JLabel();
        JLabel phoneLabel = new JLabel();
        JLabel categoryLabel = new JLabel();
        JLabel addressLabel = new JLabel();
        JLabel fechaNacimientoLabel = new JLabel();

// Crear el botón "Volver"
        JButton backButton = new JButton("Volver");
        backButton.setPreferredSize(new Dimension(100, 30));

// Agregar los componentes al panel de detalles
        detailsPanel.add(nameLabel, BorderLayout.NORTH);
        detailsPanel.add(phoneLabel, BorderLayout.WEST);
        detailsPanel.add(categoryLabel, BorderLayout.CENTER);
        detailsPanel.add(addressLabel, BorderLayout.EAST);
        detailsPanel.add(fechaNacimientoLabel, BorderLayout.SOUTH);
        detailsPanel.add(backButton, BorderLayout.PAGE_END);

// Crear un CardLayout para cambiar entre la lista de miembros y los detalles del tripulante
        CardLayout cardLayout = new CardLayout();
        JPanel cardPanel = new JPanel(cardLayout);
        cardPanel.add(crewPanel, "listaMiembros");
        cardPanel.add(detailsPanel, "detallesMiembro");

// Agregar el panel con CardLayout a la pestaña de tripulantes
        JPanel crewTab = new JPanel ( new BorderLayout ( ) );
        crewTab.add(cardPanel, BorderLayout.CENTER);

// Agregar un ActionListener al botón "Ver detalles"
        miembrosList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedMember = miembrosList.getSelectedValue();
                    if (selectedMember != null) {
// Separar el teléfono del resto de la información
                        String[] memberInfo = selectedMember.split(" ");
                        String phone = memberInfo[0];
                        // Obtener los detalles del tripulante seleccionado y mostrarlos en los JLabels correspondientes
                        ConexionBD conexionBD3 = new ConexionBD();
                        ResultSet miembro = conexionBD3.ejecutarConsulta("SELECT * FROM miembros WHERE telefono='" + phone + "'");
                        try {
                            if (miembro.next()) {
                                nameLabel.setText(miembro.getString("nombre") + " " + miembro.getString("apellido1") + " " + miembro.getString("apellido2"));
                                phoneLabel.setText("Teléfono: " + miembro.getString("telefono"));
                                categoryLabel.setText("Categoría: " + miembro.getString("categoria"));
                                addressLabel.setText("Dirección: " + miembro.getString("direccion"));
                                fechaNacimientoLabel.setText("fechaNacimiento: " + miembro.getString("fechaNacimiento"));

                                // Mostrar el panel de detalles y ocultar la lista de miembros
                                cardLayout.show(cardPanel, "detallesMiembro");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

            }
        });

// Agregar un ActionListener al botón "Volver"
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// Mostrar la lista de miembros y ocultar el panel de detalles
                cardLayout.show(cardPanel, "listaMiembros");
            }
        });

// Cerrar la conexión a la base de datos
        conexionBD2.cerrarConexion();
        // Crear un nuevo JPanel para los botones de acción
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.Y_AXIS));

// Crear los botones
        JButton editButton = new JButton("Editar");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// Manejar la acción de editar aquí
            }
        });

        JButton addButton = new JButton("Añadir");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// Manejar la acción de añadir aquí
            }
        });

        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
// Manejar la acción de eliminar aquí
            }
        });

// Agregar los botones al JPanel de botones
        buttonsPanel.add(editButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);

// Agregar el JPanel de botones al panel de la pestaña Tripulantes
        crewPanel.add(buttonsPanel, BorderLayout.EAST); // O en cualquier posición que desees


// Agregar la pestaña de tripulantes al JTabbedPane
        tabbedPane.addTab("Tripulantes", crewTab);


        // Pestaña de Pasajeros
        JPanel passengerPanel = new JPanel ( );
        passengerPanel.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de pasajeros
        JPanel tablePanel = new JPanel ( );
        tablePanel.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        tablePanel.setLayout ( new BorderLayout ( ) );

        // Tabla de pasajeros
        String[] columnNames = { "idPasajero" , "nombre" , "apellido1" , "apellido2" , "edad" , "telefono" , "ecorreo" , "direccion" , "foto" };
        Object[][] data = {
                { 1 , "Juan" , "Perez" , "Gomez" , 30 , "123456789" , "juan.perez@example.com" , "Calle 123, Ciudad" , "foto1.jpg" } ,
                { 2 , "Maria" , "Lopez" , "Gonzalez" , 25 , "987654321" , "maria.lopez@example.com" , "Avenida 456, Pueblo" , "foto2.jpg" } ,
                { 3 , "Pedro" , "Gomez" , "Rodriguez" , 40 , "456789123" , "pedro.gomez@example.com" , "Carretera 789, Villa" , "foto3.jpg" }
        };
        JTable      passengerTable      = new JTable ( data , columnNames );
        JScrollPane passengerScrollPane = new JScrollPane ( passengerTable );
        tablePanel.add ( passengerScrollPane , BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de pasajeros
        passengerPanel.add ( tablePanel , BorderLayout.CENTER );

        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addPassengerButton    = new JButton ( "Agregar" );
        JButton editPassengerButton   = new JButton ( "Editar" );
        JButton deletePassengerButton = new JButton ( "Eliminar" );
        JPanel  passengerButtonsPanel = new JPanel ( );
        passengerButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        passengerButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER , 20 , 10 ) ); // Mayor margen entre los botones
        passengerButtonsPanel.add ( addPassengerButton );
        passengerButtonsPanel.add ( editPassengerButton );
        passengerButtonsPanel.add ( deletePassengerButton );
        passengerPanel.add ( passengerButtonsPanel , BorderLayout.SOUTH );

        // Agregar el panel de pasajeros al panel de pestañas
        tabbedPane.addTab ( "Pasajeros" , passengerPanel );

        // Pestaña de Vuelos
        PestaniaVuelos flightsPanel = new PestaniaVuelos( );

        // Agregar el panel de vuelos al panel de pestañas
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