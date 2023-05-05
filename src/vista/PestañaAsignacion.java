package vista;

import javax.swing.*;
import java.awt.*;

public class PestañaAsignacion {
    public void pestañaAsignacion(JPanel assignmentsPanel, JTabbedPane tabbedPane){
        // Pestaña de Asignación
        assignmentsPanel = new JPanel ( );
        assignmentsPanel.setLayout ( new BorderLayout ( ) );
        JTable      assignmentsTable      = new JTable ( );
        JScrollPane assignmentsScrollPane = new JScrollPane ( assignmentsTable );
        assignmentsPanel.add ( assignmentsScrollPane , BorderLayout.CENTER );
        tabbedPane.addTab ( "Asignación" , assignmentsPanel );


    }
}
