import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestaniaVuelos extends JPanel {

    PestaniaVuelos() throws SQLException {

        this.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de vuelos
        JPanel tablaVuelos = new JPanel ( );
        tablaVuelos.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        tablaVuelos.setLayout ( new BorderLayout ( ) );

        // Tabla de vuelos
        String[] NombreCOlumnas = { "IdVuelo" , "IdAvión" , "Origen" , "Destino" , "Fecha", "Hora de Salida" , "Hora de llegada" };
        // Crear una instancia de la clase de conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD ( );
        // Ejecutar una consulta para obtener todos los aviones de la tabla de aviones
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM vuelos");
        DefaultTableModel contenidoTablaVuelos             = new DefaultTableModel ( new Object[][]{} , NombreCOlumnas );
        while (resultado.next()){

            ResultSet resultado2 = conexionBD.ejecutarConsulta ( "SELECT * FROM trayectos where idTrayecto="+resultado.getInt("idTrayecto") );
            resultado2.next();
            contenidoTablaVuelos.addRow(new Object[]{
                    resultado.getInt("idVuelo"),
                    resultado.getInt("idAvion"),
                    resultado2.getString("destino"),
                    resultado2.getString("origen"),
                    resultado.getString("fecha"),
                    resultado.getString("horaSalida"),
                    resultado.getString("horaLlegada")
            });

        }
        JTable            flightsTable      = new JTable ( contenidoTablaVuelos );
        JScrollPane       flightsScrollPane = new JScrollPane ( flightsTable );
        tablaVuelos.add ( flightsScrollPane , BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de vuelos
        this.add ( tablaVuelos , BorderLayout.CENTER );

        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addFlightButton     = new JButton ( "Agregar" );
        addFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirVuelo v = new anadirVuelo();
            }
        });
        JButton editFlightButton    = new JButton ( "Editar" );
        editFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = flightsTable.getSelectedRow();
                Boolean flag = false;
                String selec = "";
                try {
                    selec = flightsTable.getModel().getValueAt(row, 0).toString();
                    flag = true;
                } catch (ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un vuelo");
                }
                if(flag){
                    editarVuelo ed = new editarVuelo(selec);
                }
                
            }
        });
        JButton deleteFlightButton  = new JButton ( "Eliminar" );
        deleteFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = flightsTable.getSelectedRow();
                Boolean flag = false;
                int selec = 0;
                try {
                    selec = Integer.parseInt(flightsTable.getModel().getValueAt(row, 0).toString());
                    flag = true;
                } catch (ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un vuelo");
                }
                if(flag){
                    conexionBD.ejecutarConsulta("DELETE from vuelos where idVuelo = "+selec);
                    JOptionPane.showMessageDialog(null, "Vuelo borrado con éxito");
                    conexionBD.cerrarConexion();
                }
            }
        });
        JPanel  flightsButtonsPanel = new JPanel ( );
        flightsButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 , 10 , 10 , 10 ) ); // Margen de 10 en todos los lados
        flightsButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER , 20 , 10 ) ); // Mayor margen entre los botones
        flightsButtonsPanel.add ( addFlightButton );
        flightsButtonsPanel.add ( editFlightButton );
        flightsButtonsPanel.add ( deleteFlightButton );
        this.add ( flightsButtonsPanel , BorderLayout.SOUTH );

    }

}
