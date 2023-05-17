package vista.emergentesVuelo;

import controlador.Logomens;
import modelo.ConexionBD;
import com.toedter.calendar.JDateChooser;
import vista.PestaniaVuelos;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class editarVuelo extends JFrame {

    public editarVuelo(String selec, JTable flightsTable){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        this.setLayout(new BorderLayout());
        JLabel vueloLabel = new JLabel("VUELO");
        JPanel vueloPanel = new JPanel();
        vueloPanel.add(vueloLabel);
        vueloPanel.setBorder(new EmptyBorder(10,10,10,10));
        this.add(vueloPanel, BorderLayout.NORTH);
        JPanel datosVuelo = new JPanel();
        GridLayout grid = new GridLayout(7,2);
        grid.setVgap(10);
        grid.setHgap(10);
        datosVuelo.setBorder(new EmptyBorder(10,10,10,10));
        datosVuelo.setLayout(grid);
        JLabel idAvionLabel = new JLabel("ID AVION: ");
        JLabel idVueloLabel = new JLabel("ID VUELO: ");
        datosVuelo.add(idVueloLabel);
        JComboBox<String> idAvionesCombo = new JComboBox<>();
        JTextField idVuelosCombo = new JTextField();
        ConexionBD con = new ConexionBD();
        String query;
        ResultSet resul;
        datosVuelo.add(idVuelosCombo);
        datosVuelo.add(idAvionLabel);
        query = "SELECT idAvion from aviones where estado=1 order by 1";
        resul = con.ejecutarConsulta(query);
        while(true){
            try {
                if (!resul.next()) break;
                idAvionesCombo.addItem(String.valueOf(resul.getInt("idAvion")));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
        datosVuelo.add(idAvionesCombo);
        JLabel origenLabel = new JLabel("ORIGEN: ");
        datosVuelo.add(origenLabel);
        JTextField origenTexfield = new JTextField();
        datosVuelo.add(origenTexfield);
        JLabel destinoLabel = new JLabel("DESTINO: ");
        datosVuelo.add(destinoLabel);
        JTextField destinoTexfield = new JTextField();
        datosVuelo.add(destinoTexfield);
        JLabel fechaLabel = new JLabel("FECHA: ");
        JDateChooser fechaChooser = new JDateChooser();
        datosVuelo.add(fechaLabel);
        datosVuelo.add(fechaChooser);
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
        // Obtener los datos del vuelo seleccionado de la base de datos
        String query2 = "SELECT * FROM vuelos join trayectos on vuelos.idTrayecto=trayectos.idTrayecto WHERE idVuelo = ?";
        try {
            PreparedStatement stmt = con.getConexion().prepareStatement(query2);
            stmt.setInt(1, Integer.parseInt(selec));
            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                // Obtener la fecha del resultado de la consulta SQL
                java.sql.Date fechaSQL = result.getDate("fecha");
                // Crear un objeto java.util.Date a partir de la fecha obtenida
                java.util.Date fechaUtil = new java.util.Date(fechaSQL.getTime());
                fechaChooser.setDate (fechaUtil);
                // Establecer los valores correspondientes en los componentes de la interfaz de usuario
                idVuelosCombo.setText(String.valueOf(result.getInt("idVuelo")));
                idAvionesCombo.setSelectedItem(String.valueOf(result.getInt("idAvion")));
                origenTexfield.setText(result.getString("origen"));
                destinoTexfield.setText(result.getString("destino"));
                horaSalidaTexfield.setText(timeFormat.format(result.getTime("horaSalida")));
                horaLlegadaTexfield.setText(timeFormat.format(result.getTime("horaLlegada")));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener la fecha seleccionada del JDateChooser
                java.util.Date fechaSeleccionada = fechaChooser.getDate();
                // Convertir la fecha al formato deseado
                java.sql.Date fechaSQL = new java.sql.Date(fechaSeleccionada.getTime());
                // Convertir la fecha a una cadena en el formato deseado (por ejemplo, "yyyy-MM-dd")
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String fecha = sdf.format(fechaSeleccionada);

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
                                UPDATE vuelos
                                SET fecha = ?,
                                horaSalida = ?,
                                horaLlegada = ?,
                                idAvion = ?,
                                idVuelo = ?,
                                idTrayecto = ?
                                WHERE idVuelo = ?
                                ;
                                    """);
                    p.setString(1,fecha);
                    p.setString(2,horaSalidaTexfield.getText());
                    p.setString(3, horaLlegadaTexfield.getText());
                    p.setInt(4, Integer.parseInt((String) idAvionesCombo.getSelectedItem()));
                    p.setInt(5, Integer.parseInt(idVuelosCombo.getText()));
                    p.setInt(6,idTray);
                    p.setInt(7, Integer.parseInt(selec));
                    p.executeUpdate();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vueloPanel,"Ya existe un vuelo con ese identificador, por favor eliga un identifiador distinto");
                }
                PestaniaVuelos p = null;
                try {
                    p = new PestaniaVuelos();
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    p.actualizarTabla(flightsTable);
                }
                catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                Logomens log = new Logomens();
                log.escribirRegistro("Vuelo "+selec+" editado");
                dispose();
            }

        });
        limpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                origenTexfield.setText("");
                destinoTexfield.setText("");
                fechaChooser.setDate(null);
                horaSalidaTexfield.setText("");
                horaLlegadaTexfield.setText("");
            }
        });
        botones.add(aceptar);
        botones.add(limpiar);
        botones.setBorder(new EmptyBorder(0,0,20,0));
        this.add(botones,BorderLayout.SOUTH);
        this.add(datosVuelo,BorderLayout.CENTER);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }
}
