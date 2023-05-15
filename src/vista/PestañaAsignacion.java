package vista;

import modelo.ConexionBD;
import modelo.Miembro;
import modelo.Miembro2;
import modelo.Vuelo;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class PestañaAsignacion {
    String idAvion;
    int idVuelo = -1;
    JTextArea pasajeros = new JTextArea();
    PestañaAsignacion(JTabbedPane tabbedPane) throws SQLException {
        // Pestaña de Asignación
        JPanel assignmentsPanel = new JPanel();
        assignmentsPanel.setLayout ( new BorderLayout ( ) );
        JLabel tit = new JLabel("CONFIRMACIÓN DEL VUELO");
        tit.setFont(tit.getFont().deriveFont(30f));
        assignmentsPanel.setBorder(new EmptyBorder(30,0,0,0));
        JPanel norte = new JPanel();
        norte.add(tit);
        assignmentsPanel.add(norte,BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setBorder(new EmptyBorder(20,0,0,0));
        JPanel centerLeft = new JPanel();
        JPanel centerRight = new JPanel();
        TitledBorder borde1 = new TitledBorder("LOGÍSTICA VUELO");
        borde1.setTitleFont(borde1.getTitleFont().deriveFont(20f));
        centerLeft.setBorder(borde1);
        JLabel avionLabel = new JLabel("AVIÓN: ");
        JLabel modeloLabel = new JLabel();
        ConexionBD con = new ConexionBD();
        ResultSet result = con.ejecutarConsulta("SELECT matricula FROM aviones where estado=1");
        JComboBox<String> avionBox = new JComboBox<>();
        JComboBox<Vuelo> trayectoCombo = new JComboBox<>();
        JLabel horaVuelo = new JLabel();
        JLabel fechaVuelo = new JLabel();
        trayectoCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vuelo selec = (Vuelo) trayectoCombo.getSelectedItem();
                if (selec == null){
                    fechaVuelo.setText("");
                    horaVuelo.setText("");
                    idVuelo = -1;
                } else {
                    fechaVuelo.setText(selec.getFecha());
                    horaVuelo.setText(selec.getHora());
                    PreparedStatement p = null;
                    try {
                        p = con.getConexion().prepareStatement("""
                            select nombre, apellido1, apellido2
                            from pasajeros p
                            join pasajeros_vuelos pv
                            on p.idPasajeros = pv.idPasajeros
                            where idVuelo = ? and idAvion = ?;
                            """);
                        idVuelo = selec.getId();
                        p.setInt(1,idVuelo);
                        p.setInt(2, Integer.parseInt(idAvion));
                        ResultSet res = p.executeQuery();
                        pasajeros.setText("");
                        while (res.next()){
                            pasajeros.setText(res.getString("apellido1")+" "+res.getString("apellido2")+
                                    ", "+res.getString("nombre")+ "\n" +pasajeros.getText()  );
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                }
            }
        });
        while (result.next()){

            avionBox.addItem(result.getString("matricula"));
        }
        ActionListener listenerAvion = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selec = (String) avionBox.getSelectedItem();
                String query = "SELECT idAvion, modelo from aviones where matricula like "+"'"+selec+"'";
                ResultSet result = con.ejecutarConsulta(query);
                try {
                    result.next();
                    modeloLabel.setText(result.getString("modelo"));
                    idAvion = result.getString("idAvion");
                    String consultaTrayecto = """
                            SELECT v.idVuelo, v.fecha, v.horaSalida, destino, origen
                            FROM trayectos t
                            JOIN vuelos v
                            ON v.idTrayecto = t.idTrayecto
                            JOIN aviones a
                            ON a.idAvion = v.idAvion
                            WHERE a.idAvion ="""+ idAvion;
                    result = con.ejecutarConsulta(consultaTrayecto);
                    pasajeros.setText("");
                    trayectoCombo.removeAllItems();
                    while(result.next()){
                        Vuelo v = new Vuelo(result.getInt("idVuelo"),result.getString("destino"),result.getString("origen"),
                                result.getString("fecha"),result.getString("horaSalida"));
                        trayectoCombo.addItem(v);
                    }

                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
            };
        avionBox.addActionListener(listenerAvion);
        avionBox.setSelectedItem(avionBox.getItemAt(0));
        listenerAvion.actionPerformed(new ActionEvent(avionBox, ActionEvent.ACTION_PERFORMED, ""));

        pasajeros.setEditable(false);
        pasajeros.setLineWrap(true);
        pasajeros.setWrapStyleWord(true);
        pasajeros.setPreferredSize(new Dimension(300, 330));
        pasajeros.setBorder(new LineBorder(Color.BLACK,2));

        JLabel vueloLabel = new JLabel("VUELO: ");
        JLabel tripuLabel = new JLabel("TRIPULACIÓN: ");
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
        tripSelec.setEditable(false);
        tripSelec.setLineWrap(true);
        tripSelec.setWrapStyleWord(true);
        tripSelec.setBorder(BorderFactory.createLineBorder(Color.BLACK,2));
        tripSelec.setPreferredSize(new Dimension(200, 100));
        centerLeft.add(avionLabel);
        centerLeft.add(avionBox);
        centerLeft.add(modeloLabel);
        centerLeft.add(vueloLabel);
        centerLeft.add(trayectoCombo);
        centerLeft.add(fechaVuelo);
        JPanel tripu = new JPanel();
        tripu.setPreferredSize(new Dimension(200,200));
        tripu.add(tripuLabel);
        tripu.add(scrollPane);
        centerLeft.add(tripu);
        centerLeft.add(tripSelec);
        ArrayList<Miembro2> miembrosSelec = new ArrayList<>();
        miembrosList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting()){
                    Miembro2 selec = miembrosList.getSelectedValue();
                    if (selec!=null && miembrosSelec.size()<5){
                            miembrosSelec.add(selec);
                            tripSelec.append(selec.toString()+"\n");

                    } else if (selec!=null) {
                        UIManager.put("OptionPane.yesButtonText", "Sí");
                        int opt = JOptionPane.showConfirmDialog(assignmentsPanel, "Solo puede añadir 5 tripulantes. ¿Desea empezar de nuevo?", "Información",JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_NO_OPTION){
                            miembrosSelec.clear();
                            tripSelec.setText("");
                            miembrosList.clearSelection();
                        }
                    }
                }
            }
        });
        JButton limpiar = new JButton("Limpiar");
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                miembrosSelec.clear();
                tripSelec.setText("");
                miembrosList.clearSelection();
            }
        });
        centerLeft.add(limpiar);
        centerLeft.setPreferredSize(new Dimension(250,450));
        TitledBorder borde2 = new TitledBorder("PASAJEROS");
        borde2.setTitleFont(borde2.getTitleFont().deriveFont(20f));
        borde2.setBorder(new EmptyBorder(0,40,75,0));
        centerRight.setBorder(borde2);
        centerRight.add(pasajeros);
        center.add(centerLeft);
        center.add(centerRight);



        JPanel sur = new JPanel();
        JButton listo = new JButton("¡Listo!");
        listo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PreparedStatement p = null;
                try {
                    p = con.getConexion().prepareStatement("""
                            select idVuelo, idAvion
                            from miembros_vuelos pv
                            where idVuelo = ? and idAvion = ?;
                            """);
                    p.setInt(1, idVuelo);
                    p.setInt(2, Integer.parseInt(idAvion));
                    ResultSet res = p.executeQuery();
                    if (miembrosSelec.isEmpty() || idVuelo == -1)
                    {
                        JOptionPane.showMessageDialog(assignmentsPanel,"Seleccione un vuelo y al menos un tripulante");
                    } else {
                        if (!res.next()) {
                            Iterator<Miembro2> it = miembrosSelec.iterator();
                            while (it.hasNext()) {
                                Miembro2 m = it.next();
                                p = con.getConexion().prepareStatement("""
                            insert into miembros_vuelos
                            values(?,?,?,?)
                            """);
                            LocalDate fechaActual = LocalDate.now();
                            DateTimeFormatter formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                            String fechaComoString = fechaActual.format(formateador);
                            p.setInt(1, idVuelo);
                            p.setInt(2, Integer.parseInt(idAvion));
                            p.setInt(3, m.getId());
                            p.setString(4, fechaComoString);
                            int res1 = p.executeUpdate();
                        }
                            JOptionPane.showMessageDialog(assignmentsPanel,"Tripulantes añadidos con éxito");
                    } else {
                        UIManager.put("OptionPane.yesButtonText", "Sí");
                        int opt = JOptionPane.showConfirmDialog(assignmentsPanel, "Ya se asignó ese vuelo. ¿Desea borrar la asignación existente?", "Información", JOptionPane.YES_NO_OPTION);
                        if (opt == JOptionPane.YES_NO_OPTION) {
                            p = con.getConexion().prepareStatement("""
                            delete from miembros_vuelos
                            where idVuelo = ? and idAvion = ?;
                            """);
                            p.setInt(1, idVuelo);
                            p.setInt(2, Integer.parseInt(idAvion));
                            int res1 = p.executeUpdate();
                            miembrosSelec.clear();
                            tripSelec.setText("");
                            avionBox.setSelectedItem(avionBox.getItemAt(0));
                            miembrosList.clearSelection();

                        }
                    }
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        sur.setBorder(new EmptyBorder(0,0,20,0));
        listo.setPreferredSize(new Dimension(150,50));
        listo.setFont(listo.getFont().deriveFont(20f));
        sur.add(listo);
        assignmentsPanel.add(sur, BorderLayout.SOUTH);
        assignmentsPanel.add(center,BorderLayout.CENTER);
        tabbedPane.addTab ( "Asignación" , assignmentsPanel);


    }
}