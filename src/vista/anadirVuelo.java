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

public class anadirVuelo extends JFrame {

    public anadirVuelo(JTable flightsTable){

        this.setLayout(new BorderLayout());
        JLabel vueloLabel = new JLabel("VUELO");
        JPanel vueloPanel = new JPanel();
        vueloPanel.add(vueloLabel);
        this.add(vueloPanel, BorderLayout.NORTH);
        JPanel datosVuelo = new JPanel();
        datosVuelo.setLayout(new GridLayout(6,2));
        JLabel idAvionLabel = new JLabel("ID AVION: ");
        datosVuelo.add(idAvionLabel);
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
        datosVuelo.add(idAvionesCombo);
        JLabel origenLabel = new JLabel("DESTINO: ");
        datosVuelo.add(origenLabel);
        JTextField origenTexfield = new JTextField();
        datosVuelo.add(origenTexfield);
        JLabel destinoLabel = new JLabel("ORIGEN: ");
        datosVuelo.add(destinoLabel);
        JTextField destinoTexfield = new JTextField();
        datosVuelo.add(destinoTexfield);
        JLabel fechaLabel = new JLabel("FECHA: ");
        datosVuelo.add(fechaLabel);
        JTextField fechaTexfield = new JTextField();
        datosVuelo.add(fechaTexfield);
        JLabel horaSalidaLabel = new JLabel("HORA DE SALIDA: ");
        datosVuelo.add(horaSalidaLabel);
        JTextField horaSalidaTexfield = new JTextField();
        datosVuelo.add(horaSalidaTexfield);
        JLabel horaLlegadaLabel = new JLabel("HORA DE LLEGADA: ");
        datosVuelo.add(horaLlegadaLabel);
        JTextField horaLlegadaTexfield = new JTextField();
        datosVuelo.add(horaLlegadaTexfield);
        JButton aceptar = new JButton("ACEPTAR");
        JButton limpiar = new JButton("LIMPIAR CAMPOS");
        JPanel botones = new JPanel();
        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet resTrayectos = con.ejecutarConsulta("SELECT idTrayecto from trayectos where origen like" + "'" +
                        origenTexfield.getText() + "'" + " and destino like " + "'" + destinoTexfield.getText() + "'");
                int idTray;
                try {
                    resTrayectos.next();
                    idTray = resTrayectos.getInt("idTrayecto");
                } catch (SQLException ex) {
                    try {
                        PreparedStatement p = con.getConexion().prepareStatement("""
                        insert into trayectos(destino, origen) values(?,?);
                        """);
                        p.setString(1,destinoTexfield.getText());
                        p.setString(2,origenTexfield.getText());
                        p.executeUpdate();
                        resTrayectos = con.ejecutarConsulta("SELECT idTrayecto from trayectos where origen like" + "'" +
                                origenTexfield.getText() + "'" + " and destino like " + "'" + destinoTexfield.getText() + "'");
                        resTrayectos.next();
                        idTray = resTrayectos.getInt("idTrayecto");
                    } catch (SQLException exc) {
                        throw new RuntimeException(exc);
                    }
                }
                try {
                    PreparedStatement p = con.getConexion().prepareStatement("""
                                INSERT INTO vuelos(fecha, horaSalida, horaLlegada, idAvion, idTrayecto)
                                VALUES (?,?,?,?,?);
                                    """);
                    p.setString(1,fechaTexfield.getText());
                    p.setString(2,horaSalidaTexfield.getText());
                    p.setString(3, horaLlegadaTexfield.getText());
                    p.setInt(4, Integer.parseInt((String) idAvionesCombo.getSelectedItem()));
                    p.setInt(5,idTray);
                    p.executeUpdate();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                PestaniaVuelos p = null;
                try {
                    p = new PestaniaVuelos ();
                }
                catch (SQLException ex) {
                    throw new RuntimeException ( ex );
                }
                try {
                    p.actualizarTabla (flightsTable );
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
                origenTexfield.setText("");
                destinoTexfield.setText("");
                fechaTexfield.setText("");
                horaSalidaTexfield.setText("");
                horaLlegadaTexfield.setText("");
            }
        });
        botones.add(aceptar);
        botones.add(limpiar);
        this.add(botones,BorderLayout.SOUTH);
        this.add(datosVuelo,BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);

    }
}
