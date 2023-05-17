package vista;

import controlador.Logomens;
import modelo.ConexionBD;
import vista.emergentesPasajero.AñadirPasajero;
import vista.emergentesPasajero.EditarPasajero;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestañaPasajeros {
    public void pestañaPasajeros (JTabbedPane tabbedPane ) throws SQLException {
        // Pestaña de Pasajeros
        JPanel passengerPanel = new JPanel ( );
        passengerPanel.setLayout ( new BorderLayout ( ) );

        // Panel superior con la tabla de pasajeros
        JPanel tablePanel = new JPanel ( );
        tablePanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        tablePanel.setLayout ( new BorderLayout ( ) );

        // Tabla de pasajeros
        String[] columnNames = { "idPasajero" , "DNI", "nombre" ,"apellido1" ,"apellido2" ,
                "edad" ,"telefono" ,"Correo electrónico" ,"direccion" , "ID Vuelo"};
        ConexionBD conexionBD = new ConexionBD ( );
        // Ejecutar una consulta para obtener todos los aviones de la tabla de aviones
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM pasajeros");
        ResultSet resultado3 = conexionBD.ejecutarConsulta("SELECT edad(fechaNacimiento) as edad FROM pasajeros;");
        DefaultTableModel contenidoTablaPasajeros = new DefaultTableModel ( new Object[][]{}, columnNames );
        boolean flag;
        while (resultado.next()){
            flag = false;
            resultado3.next();
            ResultSet resultado2 = conexionBD.ejecutarConsulta ( "SELECT * FROM pasajeros_vuelos where idPasajeros="+resultado.getInt("idPasajeros"));
            while(resultado2.next()) {
                    contenidoTablaPasajeros.addRow(new Object[]{
                            resultado.getInt("idPasajeros"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado3.getString("edad"),
                            resultado.getString("telefono"),
                            resultado.getString("ecorreo"),
                            resultado.getString("direccion"),
                            resultado2.getInt("idVuelo")

                });
                flag = true;
            }
                if (!flag) {
                    contenidoTablaPasajeros.addRow(new Object[]{
                            resultado.getInt("idPasajeros"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado3.getString("edad"),
                            resultado.getString("telefono"),
                            resultado.getString("ecorreo"),
                            resultado.getString("direccion"),
                    });
                }
        }
        JTable passengerTable = new JTable (contenidoTablaPasajeros);
        passengerTable.setDefaultEditor(Object.class, null);
        JScrollPane passengerScrollPane = new JScrollPane ( passengerTable );
        tablePanel.add ( passengerScrollPane ,BorderLayout.CENTER );

        // Agregar el panel de tabla al panel principal de pasajeros
        passengerPanel.add ( tablePanel ,BorderLayout.CENTER );

        // Panel inferior con los botones de agregar, editar y eliminar
        JButton addPassengerButton = new JButton ( "Agregar" );
        JButton editPassengerButton = new JButton ( "Editar" );
        JButton deletePassengerButton = new JButton ( "Eliminar" );
        JPanel passengerButtonsPanel = new JPanel ( );


        // Agregar el panel de pasajeros al panel de pestañas
        tabbedPane.addTab ( "Pasajeros" ,passengerPanel );

        addPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AñadirPasajero v = new AñadirPasajero();
                try {
                    v.actionPerformed(e,passengerTable);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    actualizarTabla(passengerTable);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        editPassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = passengerTable.getSelectedRow();
                boolean flag = false;
                String selec = "";

                try {
                    selec = passengerTable.getModel().getValueAt(row, 0).toString();
                    flag = true;
                } catch (ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un pasajero");
                }

                if (flag) {
                    try {
                        EditarPasajero ed = new EditarPasajero();
                        ed.actionPerformed(e,selec,passengerTable);
                    }
                    catch (SQLException ex) {
                        throw new RuntimeException ( ex );
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        conexionBD.cerrarConexion();
                        actualizarTabla(passengerTable);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        deletePassengerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = passengerTable.getSelectedRow();
                Boolean flag = false;
                int selec = 0;
                try {
                    selec = Integer.parseInt(passengerTable.getModel().getValueAt(row, 0).toString());
                    flag = true;
                } catch (ArrayIndexOutOfBoundsException ex){
                    JOptionPane.showMessageDialog(null, "Por favor, seleccione un vuelo");
                }
                if(flag){
                    ConexionBD conexionBD1 = new ConexionBD ();
                    conexionBD1.ejecutarConsulta("DELETE from pasajeros where idPasajeros = "+selec);
                    JOptionPane.showMessageDialog(null, "Pasajero borrado con éxito");
                    Logomens log = new Logomens ();
                    log.escribirRegistro("Pasajero "+selec+" eliminado");
                    try {
                        conexionBD.cerrarConexion();
                        actualizarTabla(passengerTable);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        passengerButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        passengerButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER ,20 ,10 ) ); // Mayor margen entre los botones
        passengerButtonsPanel.add ( addPassengerButton );
        passengerButtonsPanel.add ( editPassengerButton );
        passengerButtonsPanel.add ( deletePassengerButton );
        passengerPanel.add ( passengerButtonsPanel ,BorderLayout.SOUTH );
    }
    public void actualizarTabla(JTable passengerTable) throws SQLException {
        // Crear una instancia de la clase de conexión a la base de datos
        ConexionBD conexionBD = new ConexionBD();
        // Ejecutar una consulta para obtener todos los pasajeros de la tabla de pasajeros
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM pasajeros");
        ResultSet resultado3 = conexionBD.ejecutarConsulta("SELECT edad(fechaNacimiento) as edad FROM pasajeros");
        DefaultTableModel modeloTabla = (DefaultTableModel) passengerTable.getModel();
        // Limpiar el modelo de la tabla
        modeloTabla.setRowCount(0);
        // Agregar los pasajeros al modelo de la tabla
        boolean flag;
        while (resultado.next()) {
            flag = false;
            resultado3.next();
            ResultSet resultado2 = conexionBD.ejecutarConsulta("SELECT * FROM pasajeros_vuelos WHERE idPasajeros = " + resultado.getInt("idPasajeros"));
            while (resultado2.next()) {
                modeloTabla.addRow(new Object[]{
                        resultado.getInt("idPasajeros"),
                        resultado.getString("dni"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido1"),
                        resultado.getString("apellido2"),
                        resultado3.getString("edad"),
                        resultado.getString("telefono"),
                        resultado.getString("ecorreo"),
                        resultado.getString("direccion"),
                        resultado2.getInt("idVuelo")
                });
                flag = true;
            }
            if (!flag) {
                modeloTabla.addRow(new Object[]{
                        resultado.getInt("idPasajeros"),
                        resultado.getString("dni"),
                        resultado.getString("nombre"),
                        resultado.getString("apellido1"),
                        resultado.getString("apellido2"),
                        resultado3.getString("edad"),
                        resultado.getString("telefono"),
                        resultado.getString("ecorreo"),
                        resultado.getString("direccion"),
                        ""
                });
            }
        }
        // Cerrar la conexión a la base de datos
        conexionBD.cerrarConexion();
    }

}
