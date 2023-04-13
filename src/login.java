import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.sql.*;

public class login {

    public void mostrar() {
        JFrame frame = new JFrame("Inicio de sesión");
        frame.setSize(330, 175);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        panel.setLayout(null);


        InputStream is = getClass().getResourceAsStream("Recursos/fuentes/FuenteAeropuerto.ttf"); // Reemplaza "/ruta/roboto.ttf" con la ruta real del archivo de fuente Roboto en tu proyecto
        Font robotoFont = null;
        try {
            robotoFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(14f); // Tamaño de fuente 14
        } catch (Exception e) {
            e.printStackTrace();
        }


        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(20, 20, 80, 25);
        userLabel.setFont(robotoFont);
        panel.add(userLabel);

        JTextField nombreUsuario = new JTextField(20);
        nombreUsuario.setBounds(110, 20, 160, 25);
        nombreUsuario.setFont(robotoFont);
        panel.add(nombreUsuario);

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setBounds(20, 60, 80, 25);
        contrasenaLabel.setFont(robotoFont);
        panel.add(contrasenaLabel);

        JPasswordField contrasenaText = new JPasswordField(20);
        contrasenaText.setBounds(110, 60, 160, 25);
        contrasenaText.setFont(robotoFont);
        panel.add(contrasenaText);


        JButton botonEntrar = new JButton("Entrar");
        botonEntrar.setBounds(120, 100, 80, 30);
        botonEntrar.setFont(robotoFont);
        panel.add(botonEntrar);






        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public Connection getConnection(){
        try {
            //El método forName() carga el driver en el programa
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
        }
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/Proy3TE4","root","123456");
            System.out.println("Tenemos conexión...");
            Statement encapsulaCons = con.createStatement();
            String consulta = "SELECT * FROM vuelos";
            ResultSet resulCons = encapsulaCons.executeQuery(consulta);
            System.out.println("Cabecera del listado a imprimir");
            while (resulCons.next()) { //Recorre registro a registro el resultado obtenido
                System.out.println("Campo " + resulCons.getString("fecha"));
            }

            encapsulaCons.close();
            con.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    public void entradaCorrecta(){

    }

    public void entradaIncorrecta(){

    }

}
