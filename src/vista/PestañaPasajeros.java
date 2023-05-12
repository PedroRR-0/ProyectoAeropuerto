package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        String[] columnNames = { "idpasajeros" ,"nombre" ,"apellido1" ,"apellido2" ,"fechanacimiento" ,"telefono" ,"ecorreo" ,"direccion" ,"foto" };
        Object[][] data = obtenerDatosDeLaBaseDeDatos(); // Obtén los datos de la base de datos
        JTable tabla = new JTable(data, columnNames);
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable passengerTable = new JTable(tableModel);

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
                            resultado.getString("direccion")
                    });
                }
        }
        JTable passengerTable = new JTable (contenidoTablaPasajeros);
        passengerTable.setDefaultEditor(Object.class, null);
        JScrollPane passengerScrollPane = new JScrollPane(passengerTable);
        tablePanel.add(passengerScrollPane, BorderLayout.CENTER);

        // Cargar los datos de la base de datos en la tabla
        ConexionBD conexionBD = new ConexionBD();
        ResultSet resultado = conexionBD.ejecutarConsulta("SELECT * FROM pasajeros");
        try {
            while (resultado.next()) {
                Object[] rowData = new Object[columnNames.length];
                for (int i = 0; i < columnNames.length; i++) {
                    rowData[i] = resultado.getObject(columnNames[i]);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        conexionBD.cerrarConexion();

        // Agregar el panel de tabla al panel principal de pasajeros
        passengerPanel.add(tablePanel, BorderLayout.CENTER);

            JButton agregar = new JButton ( "Agregar" );
        agregar.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){


            }
        });

        JButton editar = new JButton ( "Editar" );

        editar.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){


            }
        });

        JButton eliminar = new JButton ( "Eliminar" );
        eliminar.addActionListener(new ActionListener(){
            public void actionPerformed( ActionEvent e ){


            }
        });


        JPanel passengerButtonsPanel = new JPanel ( );
        passengerButtonsPanel.setBorder ( BorderFactory.createEmptyBorder ( 10 ,10 ,10 ,10 ) ); // Margen de 10 en todos los lados
        passengerButtonsPanel.setLayout ( new FlowLayout ( FlowLayout.CENTER ,20 ,10 ) ); // Mayor margen entre los botones
        passengerButtonsPanel.add ( agregar );
        passengerButtonsPanel.add ( editar );
        passengerButtonsPanel.add ( eliminar );
        passengerPanel.add ( passengerButtonsPanel ,BorderLayout.SOUTH );

        // Agregar el panel de pasajeros al panel de pestañas
        tabbedPane.addTab ( "Pasajeros" ,passengerPanel );
    }


}
