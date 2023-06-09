package vista;

import controlador.Logomens;
import modelo.ConexionBD;
import vista.emergentesVuelo.anadirVuelo;
import vista.emergentesVuelo.editarVuelo;

import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestaniaVuelos extends JPanel {

    public PestaniaVuelos ( ) throws SQLException {
        obtenerVuelos ();
    }
    public void obtenerVuelos() throws SQLException {
        // Formato de fecha y hora
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        this.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de vuelos
        JPanel tablaVuelos = new JPanel ( );
        tablaVuelos.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        tablaVuelos.setLayout ( new BorderLayout ( ) );

        // Tabla de vuelos
        String[] NombreCOlumnas = { "IdVuelo" ,"IdAvión" ,"Origen" ,"Destino" ,"Fecha" ,"Hora de Salida" ,"Hora de llegada" };
        // Crear una instancia de la clase de conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD ( );
        // Ejecutar una consulta para obtener todos los aviones de la tabla de aviones
        ResultSet resultado = conexionBD.ejecutarConsulta ( "SELECT * FROM vuelos" );
        DefaultTableModel contenidoTablaVuelos = new DefaultTableModel ( new Object[][]{} ,NombreCOlumnas );
        while (resultado.next()) {
            ResultSet resultado2 = conexionBD.ejecutarConsulta("SELECT * FROM trayectos where idTrayecto="+resultado.getInt("idTrayecto"));
            resultado2.next();
            String fecha = dateFormat.format(resultado.getDate("fecha"));
            String horaSalida = timeFormat.format(resultado.getTime("horaSalida"));
            String horaLlegada = timeFormat.format(resultado.getTime("horaLlegada"));
            contenidoTablaVuelos.addRow(new Object[]{resultado.getInt("idVuelo"), resultado.getInt("idAvion"), resultado2.getString("origen"), resultado2.getString("destino"), fecha, horaSalida, horaLlegada});
        }

        JTable flightsTable = new JTable ( contenidoTablaVuelos );
        JScrollPane flightsScrollPane = new JScrollPane ( flightsTable );
        tablaVuelos.add ( flightsScrollPane ,BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de vuelos
        this.add ( tablaVuelos ,BorderLayout.CENTER );
        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addFlightButton     = new JButton ( "Agregar" );
        addFlightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirVuelo v = new anadirVuelo(flightsTable);
                try {
                    actualizarTabla(flightsTable);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
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
                    editarVuelo ed = new editarVuelo(selec,flightsTable);
                    try {
                        conexionBD.cerrarConexion();
                        actualizarTabla(flightsTable);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
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
                    ConexionBD conexionBD1 = new ConexionBD ();
                    conexionBD1.ejecutarConsulta("DELETE from vuelos where idVuelo = "+selec);
                    JOptionPane.showMessageDialog(null, "Vuelo borrado con éxito");
                    Logomens log = new Logomens ();
                    log.escribirRegistro("Vuelo "+selec+" eliminado");
                    try {
                        conexionBD.cerrarConexion();
                        actualizarTabla(flightsTable);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
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
    public void actualizarTabla(JTable flightsTable) throws SQLException {
        // Crear una instancia de la clase de conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD();
        // Ejecutar una consulta para obtener todos los vuelos de la tabla de vuelos
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM vuelos");
        DefaultTableModel modeloTabla = (DefaultTableModel) flightsTable.getModel();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Agregar los vuelos al modelo de la tabla
        while (resultado.next()) {
            ResultSet resultado2 = conexionBD.ejecutarConsulta("SELECT * FROM trayectos where idTrayecto="+resultado.getInt("idTrayecto"));
            resultado2.next();
            String fecha = dateFormat.format(resultado.getDate("fecha"));
            String horaSalida = timeFormat.format(resultado.getTime("horaSalida"));
            String horaLlegada = timeFormat.format(resultado.getTime("horaLlegada"));
            modeloTabla.addRow(new Object[]{resultado.getInt("idVuelo"), resultado.getInt("idAvion"), resultado2.getString("origen"), resultado2.getString("destino"), fecha, horaSalida, horaLlegada});
        }
        // Cerrar la conexión a la base de datos
        conexionBD.cerrarConexion();
    }
}
