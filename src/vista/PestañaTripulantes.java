package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestañaTripulantes{
    public void pestañaTripulantes(JPanel crewPanel, JTabbedPane tabbedPane) throws SQLException{
        // Pestaña Tripulantes
        // Crear un campo de texto para mostrar los miembros
        JList < String > miembrosList=new JList <>();

        // Crear un modelo de lista por defecto y agregarlo a la lista
        DefaultListModel < String > miembrosModel=new DefaultListModel <>();
        miembrosList.setModel(miembrosModel);

        // Agregar la lista de miembros al panel de la pestaña tripulantes
        crewPanel = new JPanel(new BorderLayout()); // Cambiar el layout a BorderLayout
        JScrollPane scrollPane = new JScrollPane(miembrosList); // Agregar un JScrollPane para permitir desplazamiento
        scrollPane.setPreferredSize(new Dimension(200, 200)); // Establecer tamaño de la lista
        crewPanel.add(scrollPane, BorderLayout.CENTER); // Agregar el JScrollPane al centro

        // Obtener los datos de la tabla "miembros" y agregarlos al modelo de lista
        ConexionBD conexionBD2=new ConexionBD();
        ResultSet salida=conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
        while ( salida.next() ) {
            String telefono=salida.getString("telefono");
            String nombre=salida.getString("nombre");
            String apellido1=salida.getString("apellido1");
            String apellido2=salida.getString("apellido2");
            String categoria=salida.getString("categoria");
            String miembro=telefono+" "+nombre+" "+apellido1+" "+apellido2+" ("+categoria+")";
            miembrosModel.addElement(miembro);
        }

        // Texto "Tripulación" centrado con margen superior
        JLabel titleLabel = new JLabel("Tripulación");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Cambiar la fuente y el tamaño
        titleLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar el texto
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // Margen superior de 20 unidades
        crewPanel.add(titleLabel, BorderLayout.NORTH); // Agregar el título al norte



        // Crear los JLabels con la información del tripulante
        JLabel photolabel= new JLabel (  );
        JLabel nameLabel=new JLabel();
        JLabel correo=new JLabel();
        JLabel apellido1=new JLabel();
        JLabel apellido2=new JLabel();
        JLabel phoneLabel=new JLabel();
        JLabel categoryLabel=new JLabel();
        JLabel addressLabel=new JLabel();
        JLabel fechaNacimientoLabel=new JLabel();

        // Crear el botón "Volver"
        JButton backButton=new JButton("Volver");
        backButton.setPreferredSize(new Dimension(100,30));

// Crear un panel para los detalles del tripulante
        JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Agregar un borde
        detailsPanel.setBackground(Color.RED); // Establecer el fondo blanco

// Crear un panel para los detalles del tripulante
        JPanel detailsPanel2 = new JPanel(new GridBagLayout());
        detailsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Agregar un borde
        detailsPanel2.setBackground(Color.lightGray); // Establecer el fondo blanco

// Crear un GridBagConstraints para controlar la colocación de los componentes
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5); // Agregar espacio entre los componentes

// Crear un JPanel para contener los detalles del tripulante
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.white);
        photolabel.setHorizontalAlignment(JLabel.CENTER); // Centrar la imagen horizontalmente
        photolabel.setVerticalAlignment(JLabel.CENTER); // Centrar la imagen verticalmente

// Agregar los JLabels con la información del tripulante al panel de detalles


        infoPanel.add(new JLabel(), constraints);
        constraints.gridx++;
        infoPanel.add(photolabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Nombre: "), constraints);
        constraints.gridx++;
        infoPanel.add(nameLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Primer Apellido: "), constraints);
        constraints.gridx++;
        infoPanel.add(apellido1, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Segundo Apellido: "), constraints);
        constraints.gridx++;
        infoPanel.add(apellido2, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Teléfono: "), constraints);
        constraints.gridx++;
        infoPanel.add(phoneLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Correo electrónico: "), constraints);
        constraints.gridx++;
        infoPanel.add(correo, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Categoría: "), constraints);
        constraints.gridx++;
        infoPanel.add(categoryLabel, constraints);


        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Dirección: "), constraints);
        constraints.gridx++;
        infoPanel.add(addressLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy++;
        infoPanel.add(new JLabel("Fecha de nacimiento: "), constraints);
        constraints.gridx++;
        infoPanel.add(fechaNacimientoLabel, constraints);

// Crear un JScrollPane para permitir desplazamiento en el panel de detalles
        JScrollPane infoScrollPane = new JScrollPane(infoPanel);

// Agregar el JScrollPane al centro del panel de detalles
        detailsPanel.add(infoScrollPane, BorderLayout.CENTER);

// Establecer el tamaño preferido del botón "Volver"
        backButton.setPreferredSize(new Dimension(100, 30));

// Agregar el botón "Volver" al panel de detalles en la parte inferior
        detailsPanel.add(backButton, BorderLayout.PAGE_END);


        // Crear un CardLayout para cambiar entre la lista de miembros y los detalles del tripulante
        CardLayout cardLayout=new CardLayout();
        JPanel cardPanel=new JPanel(cardLayout);
        cardPanel.add(crewPanel,"listaMiembros");
        cardPanel.add(detailsPanel,"detallesMiembro");

        // Agregar el panel con CardLayout a la pestaña de tripulantes
        JPanel crewTab=new JPanel(new BorderLayout());
        crewTab.add(cardPanel,BorderLayout.CENTER);
        // Crear el JLabel para la foto del miembro
        JLabel photoLabel = new JLabel();
        // Agregar un ActionListener al botón "Ver detalles"
        miembrosList.addListSelectionListener(new ListSelectionListener(){
            public void valueChanged( ListSelectionEvent e ){
                if(!e.getValueIsAdjusting()){
                    String selectedMember=miembrosList.getSelectedValue();

                    if(selectedMember!=null){
                        // Separar el teléfono del resto de la información
                        String[] memberInfo=selectedMember.split(" ");
                        String phone=memberInfo[ 0 ];
                        // Obtener los detalles del tripulante seleccionado y mostrarlos en los JLabels correspondientes
                        ConexionBD conexionBD3=new ConexionBD();
                        ResultSet miembro=conexionBD3.ejecutarConsulta("SELECT * FROM miembros WHERE telefono='"+phone+"'");
                        try {
                            if(miembro.next()){
                                nameLabel.setText(miembro.getString("nombre"));
                                phoneLabel.setText(miembro.getString("telefono"));
                                categoryLabel.setText(miembro.getString("categoria"));
                                addressLabel.setText(miembro.getString("direccion"));
                                fechaNacimientoLabel.setText(miembro.getString("fechaNacimiento"));
                                apellido1.setText ( miembro.getString ( "apellido1" ) );
                                apellido2.setText ( miembro.getString ( "apellido2" ) );
                                correo.setText ( miembro.getString ( "ecorreo" ) );
                                try {
                                    int telefono = miembro.getInt ( "telefono" );
                                    ImageIcon imagen = conexionBD3.obtenerImagen ( telefono );
                                    photolabel.setSize ( 150 ,150 );
                                    photolabel.setIcon ( imagen );
                                    photolabel.setHorizontalAlignment(JLabel.CENTER); // Centrar la imagen horizontalmente
                                    photolabel.setVerticalAlignment(JLabel.CENTER); // Centrar la imagen verticalmente
                                    photoLabel.setSize ( 150,150 );
                                    photoLabel.setIcon(imagen);
                                    photoLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar la imagen horizontalmente
                                    photoLabel.setVerticalAlignment(JLabel.CENTER); // Centrar la imagen verticalmente
                                }
                                catch (NullPointerException ex) {
                                    photolabel.setIcon ( null );
                                    System.out.println ("Este miembro no tiene foto." );
                                }

                            }
                        }
                        catch (SQLException ex) {
                            ex.printStackTrace();
                        }


                    }
                }

            }
        });

        // Agregar un ActionListener al botón "Volver"
        backButton.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){
                // Mostrar la lista de miembros y ocultar el panel de detalles
                cardLayout.show(cardPanel,"listaMiembros");
            }
        });

        // Cerrar la conexión a la base de datos
        conexionBD2.cerrarConexion();
        // Crear un nuevo JPanel para los botones de acción
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // Crear los botones
        JButton editButton=new JButton("Editar");
        editButton.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){
                String selectedMember=miembrosList.getSelectedValue();
                if(selectedMember!=null) {
                    EditarTripulante editar = new EditarTripulante();
                    editar.actionPerformed(e, selectedMember, miembrosModel );
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un tripulante.");
                }
            }
        });

        JButton detallesButton=new JButton("Detalles");
        detallesButton.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){
                String selectedMember=miembrosList.getSelectedValue();
                if(selectedMember!=null) {
                    cardLayout.show(cardPanel, "detallesMiembro");
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un tripulante.");
                }
            }
        });

        JButton addButton=new JButton("Añadir");
        addButton.addActionListener(new ActionListener(){

            public void actionPerformed( ActionEvent e ){
                AñadirTripulante Añadir=new AñadirTripulante();
                Añadir.actionPerformed(e,miembrosModel);
            }
        });

        JButton deleteButton=new JButton("Eliminar");
        deleteButton.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){
                String selectedMember=miembrosList.getSelectedValue();
                if(selectedMember!=null) {
                    EliminarTripulante Eliminar=new EliminarTripulante();
                    Eliminar.actionPerformed(e,miembrosList,miembrosModel);
                }else {
                    JOptionPane.showMessageDialog(null, "Debe seleccionar un tripulante.");
                }

            }
        });

        // Agregar los botones al JPanel de botones
        buttonsPanel.add(editButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(detallesButton);

        // Crear un JPanel para contener los botones y el espacio
        JPanel buttonsAndSpacePanel = new JPanel(new BorderLayout());

        // Agregar el JPanel de botones al JPanel contenedor
        buttonsAndSpacePanel.add(buttonsPanel, BorderLayout.NORTH);
        photoLabel.setHorizontalAlignment(JLabel.CENTER); // Centrar la imagen horizontalmente
        photoLabel.setVerticalAlignment(JLabel.CENTER); // Centrar la imagen verticalmente


        // Agregar el espacio al JPanel contenedor
        buttonsAndSpacePanel.add(photoLabel, BorderLayout.CENTER);

        // Agregar el JPanel contenedor al panel de la pestaña Tripulantes
        crewPanel.add(buttonsAndSpacePanel, BorderLayout.SOUTH);




        // Agregar la pestaña de tripulantes al JTabbedPane
        tabbedPane.addTab("Tripulantes",crewTab);



    }
}
