package vista;

import modelo.ConexionBD;
import modelo.Miembro;
import modelo.Miembro2;
import modelo.Vuelo;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestañaAsignacion {
    PestañaAsignacion(JTabbedPane tabbedPane) throws SQLException {
        // Pestaña de Asignación
        JPanel assignmentsPanel = new JPanel();
        assignmentsPanel.setLayout ( new BorderLayout ( ) );
        JLabel tit = new JLabel("CONFIRMACIÓN DEL VUELO");
        JPanel norte = new JPanel();
        norte.add(tit);
        assignmentsPanel.add(norte,BorderLayout.NORTH);
        JPanel center = new JPanel();
        JPanel centerLeft = new JPanel();
        JPanel centerRight = new JPanel();
        JLabel logistica = new JLabel("Logistica vuelo");
        JLabel avionLabel = new JLabel("AVIÓN: ");
        JLabel modeloLabel = new JLabel();
        ConexionBD con = new ConexionBD();
        ResultSet result = con.ejecutarConsulta("SELECT matricula FROM aviones");
        JComboBox<String> avionBox = new JComboBox<>();
        JComboBox<Vuelo> trayectoCombo = new JComboBox<>();
        while (result.next()){

            avionBox.addItem(result.getString("matricula"));
        }
        avionBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idAvion;
                String selec = (String) avionBox.getSelectedItem();
                String query = "SELECT idAvion, modelo from aviones where matricula like "+"'"+selec+"'";
                ResultSet result = con.ejecutarConsulta(query);
                try {
                    result.next();
                    modeloLabel.setText(result.getString("modelo"));
                    idAvion = result.getString("idAvion");
                    String consultaTrayecto = """
                            SELECT v.fecha, v.horaSalida, destino, origen
                            FROM trayectos t
                            JOIN vuelos v
                            ON v.idTrayecto = t.idTrayecto
                            JOIN aviones a
                            ON a.idAvion = v.idAvion
                            WHERE a.idAvion ="""+ idAvion;
                    result = con.ejecutarConsulta(consultaTrayecto);
                    trayectoCombo.removeAllItems();
                    while(result.next()){
                        Vuelo v = new Vuelo(result.getString("destino"),result.getString("origen"),
                                result.getString("fecha"),result.getString("horaSalida"));
                        trayectoCombo.addItem(v);
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JLabel horaVuelo = new JLabel();
        JLabel fechaVuelo = new JLabel();
        trayectoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vuelo selec = (Vuelo) trayectoCombo.getSelectedItem();
                if (selec == null){
                    fechaVuelo.setText("");
                    horaVuelo.setText("");
                } else {
                    fechaVuelo.setText(selec.getFecha());
                    horaVuelo.setText(selec.getHora());
                }
            }
        });

        JLabel vueloLabel = new JLabel("VUELO: ");
        JLabel tripuLabel = new JLabel("EL TRIPÓN: ");
        centerLeft.add(logistica);
        centerLeft.add(avionLabel);
        centerLeft.add(avionBox);
        centerLeft.add(modeloLabel);
        centerLeft.add(vueloLabel);
        centerLeft.add(trayectoCombo);
        centerLeft.add(fechaVuelo);
        centerLeft.add(horaVuelo);
        centerLeft.add(tripuLabel);
        // Crear un campo de texto para mostrar los miembros
        JList<Miembro2> miembrosList=new JList<>();

        // Crear un modelo de lista por defecto y agregarlo a la lista
        DefaultListModel <Miembro2> miembrosModel=new DefaultListModel <>();
        miembrosList.setModel(miembrosModel);
        JScrollPane scrollPane=new JScrollPane(miembrosList); // Agregar un JScrollPane para permitir desplazamiento

        // Obtener los datos de la tabla "miembros" y agregarlos al modelo de lista
        ConexionBD conexionBD2=new ConexionBD();
        ResultSet salida=conexionBD2.ejecutarConsulta("SELECT * FROM miembros");
        while ( salida.next() ) {
            Miembro2 miembro = new Miembro2(salida.getInt("idTripulacion"),
                    salida.getString("nombre"),
                    salida.getString("apellido1"),
                    salida.getString("apellido2"));
            miembrosModel.addElement(miembro);
        }
        JTextArea tripSelec = new JTextArea();
        miembrosList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()){
                    Miembro2 selec = miembrosList.getSelectedValue();
                    if (selec!=null){
                        if (miembrosModel.getSize()<5) {
                            tripSelec.setText(selec + "; " + tripSelec.getText());
                        }
                    }
                }
            }
        });
        centerLeft.add(scrollPane);
        centerLeft.add(tripSelec);
        center.add(centerLeft);
        center.add(centerRight);
        assignmentsPanel.add(center,BorderLayout.CENTER);
        tabbedPane.addTab ( "Asignación" , assignmentsPanel);


    }
}