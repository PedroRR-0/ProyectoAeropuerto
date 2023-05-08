package vista;

import modelo.ConexionBD;

import javax.swing.*;
import java.awt.*;
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
        JLabel avion = new JLabel("AVIÓN: ");
        ConexionBD con = new ConexionBD();
        ResultSet result = con.ejecutarConsulta("SELECT matricula FROM aviones");
        JComboBox<String> avionBox = new JComboBox<>();
        while (result.next()){

            avionBox.addItem(result.getString("matricula"));
        }
        centerLeft.add(logistica);
        centerLeft.add(avion);
        centerLeft.add(avionBox);
        center.add(centerLeft);
        center.add(centerRight);
        assignmentsPanel.add(center,BorderLayout.CENTER);
        tabbedPane.addTab ( "Asignación" , assignmentsPanel);


    }
}
