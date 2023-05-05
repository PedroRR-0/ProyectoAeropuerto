package vista;

import modelo.ConexionBD;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PestañaAviones {
    public void PestañaAviones ( JPanel planesPanel ,JTabbedPane tabbedPane ) throws SQLException {
        // Pestaña de Aviones
        planesPanel = new JPanel ( new GridLayout ( 4 ,1 ) ); // GridLayout con cuatro filas y una columna
        planesPanel.setBackground ( Color.WHITE );

        // Título de la pestaña
        tabbedPane.addTab ( "Aviones" ,planesPanel );

        // Agregar etiquetas de texto encima de cada tabla
        JLabel avionesOperativosLabel = new JLabel ( "Aviones Operativos" );
        avionesOperativosLabel.setFont ( new Font ( "Arial" ,Font.BOLD ,16 ) );
        avionesOperativosLabel.setHorizontalAlignment ( JLabel.CENTER );
        avionesOperativosLabel.setBorder ( new EmptyBorder ( 0 ,10 ,10 ,10 ) );
        avionesOperativosLabel.setForeground ( Color.blue );
        planesPanel.add ( avionesOperativosLabel );

        // Tabla de Aviones Operativos
        DefaultTableModel tablaAvionesOperativos = new DefaultTableModel ( new Object[][]{} ,new String[]{ "Nº del Avión" ,"Matrícula" ,"Modelo" ,"Número de asientos" } );
        JTable planesTableOperativos = new JTable ( tablaAvionesOperativos );
        planesTableOperativos.setFont ( new Font ( "Arial" ,Font.PLAIN ,14 ) );
        planesTableOperativos.getTableHeader ( ).setFont ( new Font ( "Arial" ,Font.BOLD ,14 ) );
        planesTableOperativos.getTableHeader ( ).setBackground ( Color.LIGHT_GRAY );
        planesTableOperativos.getTableHeader ( ).setForeground ( Color.black );
        planesTableOperativos.setRowHeight ( 30 );
        planesTableOperativos.setDefaultRenderer ( Object.class ,new DefaultTableCellRenderer ( ) {
            @Override
            public Component getTableCellRendererComponent ( JTable table ,Object value ,boolean isSelected ,boolean hasFocus ,int row ,int column ) {
                Component c = super.getTableCellRendererComponent ( table ,value ,isSelected ,hasFocus ,row ,column );
                c.setBackground ( row % 2==0 ? Color.WHITE : Color.LIGHT_GRAY );
                return c;
            }
        } );
        JScrollPane avionesOperativosScrollPane = new JScrollPane ( planesTableOperativos );
        avionesOperativosScrollPane.setBorder ( new EmptyBorder ( 0 ,10 ,0 ,10 ) );
        planesPanel.add ( avionesOperativosScrollPane );

        JLabel avionesNoOperativosLabel = new JLabel ( "Aviones No Operativos" );
        avionesNoOperativosLabel.setFont ( new Font ( "Arial" ,Font.BOLD ,16 ) );
        avionesNoOperativosLabel.setHorizontalAlignment ( JLabel.CENTER );
        avionesNoOperativosLabel.setBorder ( new EmptyBorder ( 10 ,10 ,10 ,10 ) );
        avionesNoOperativosLabel.setForeground ( Color.blue );

        planesPanel.add ( avionesNoOperativosLabel );

        // Tabla de Aviones No Operativos
        DefaultTableModel tablaAvionesNoOperativos = new DefaultTableModel ( new Object[][]{} ,new String[]{ "Nº del Avión" ,"Matrícula" ,"Modelo" ,"Número de asientos" } );
        JTable planesTableNoOperativos = new JTable ( tablaAvionesNoOperativos );
        planesTableNoOperativos.setFont ( new Font ( "Arial" ,Font.PLAIN ,14 ) );
        planesTableNoOperativos.getTableHeader ( ).setFont ( new Font ( "Arial" ,Font.BOLD ,14 ) );
        planesTableNoOperativos.getTableHeader ( ).setBackground ( Color.LIGHT_GRAY );
        planesTableNoOperativos.getTableHeader ( ).setForeground ( Color.black );
        planesTableNoOperativos.setRowHeight ( 30 );
        planesTableNoOperativos.setDefaultRenderer ( Object.class ,new DefaultTableCellRenderer ( ) {
            @Override
            public Component getTableCellRendererComponent ( JTable table ,Object value ,boolean isSelected ,boolean hasFocus ,int row ,int column ) {
                Component c = super.getTableCellRendererComponent ( table ,value ,isSelected ,hasFocus ,row ,column );
                c.setBackground ( row % 2==0 ? Color.WHITE : Color.LIGHT_GRAY );
                return c;
            }
        } );

        JScrollPane avionesNoOperativosScrollPane = new JScrollPane ( planesTableNoOperativos );
        avionesNoOperativosScrollPane.setBorder ( new EmptyBorder ( 0 ,10 ,0 ,10 ) );
        planesPanel.add ( avionesNoOperativosScrollPane );

        // Obtener los datos de la base de datos para llenar las tablas
        ConexionBD conexion = new ConexionBD ( );
        ResultSet resultadosOperativos = conexion.ejecutarConsulta ("SELECT * FROM aviones WHERE estado=1" );
        ResultSet resultadosNoOperativos = conexion.ejecutarConsulta ("SELECT * FROM aviones WHERE estado=0" );

        // Llenar la tabla de Aviones Operativos
        while ( resultadosOperativos.next ( ) ) {
            tablaAvionesOperativos.addRow ( new Object[]{ resultadosOperativos.getString ( "idAvion" ) ,resultadosOperativos.getString ( "matricula" ) ,resultadosOperativos.getString ( "modelo" ) ,resultadosOperativos.getString ( "numAsientos" ) } );
        }

        // Llenar la tabla de Aviones No Operativos
        while ( resultadosNoOperativos.next ( ) ) {
            tablaAvionesNoOperativos.addRow ( new Object[]{ resultadosNoOperativos.getString ( "idAvion" ) ,resultadosNoOperativos.getString ( "matricula" ) ,resultadosNoOperativos.getString ( "modelo" ) ,resultadosNoOperativos.getString ( "numAsientos" ) } );
        }

        // Cerrar la conexión a la base de datos
        conexion.cerrarConexion ( );
    }
}