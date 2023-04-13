import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Login {
    public void cargarUsuariosDesdeXml(Connection con) {
        try {
            // Cargar el archivo XML
            File archivoXml = new File("C:\\Users\\lhuji\\Desktop\\ProyectoAeropuerto\\Recursos\\UsuariosHoy.xml");
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(archivoXml);

            // Obtener la lista de usuarios del archivo XML
            NodeList listaUsuarios = doc.getElementsByTagName("usuario");

            // Recorrer la lista de usuarios y agregarlos a la base de datos
            for (int i = 0; i < listaUsuarios.getLength(); i++) {
                Node nodoUsuario = listaUsuarios.item(i);
                if (nodoUsuario.getNodeType() == Node.ELEMENT_NODE) {
                    Element elementoUsuario = (Element) nodoUsuario;
                    String usuario = elementoUsuario.getElementsByTagName("deno").item(0).getTextContent();
                    String contrasenia = elementoUsuario.getElementsByTagName("pass").item(0).getTextContent();
                    PreparedStatement stmt = con.prepareStatement("INSERT INTO `proy3te4`.`usuario` (`NOMBRE`, `CONTRASENIA`) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = NOMBRE;");
                    stmt.setString(1, usuario);
                    stmt.setString(2, contrasenia);
                    stmt.executeUpdate();
                }
            }
            System.out.println("Usuarios cargados desde el archivo XML");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/proy3te4","root","root");
            return con;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean mostrar() {
        JFrame frame = new JFrame("Inicio de sesión");
        frame.setSize(330, 175);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        panel.setLayout(null);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setBounds(20, 20, 80, 25);
        panel.add(userLabel);

        JTextField nombreUsuario = new JTextField(20);
        nombreUsuario.setBounds(110, 20, 160, 25);
        panel.add(nombreUsuario);

        JLabel contrasenaLabel = new JLabel("Contraseña:");
        contrasenaLabel.setBounds(20, 60, 80, 25);
        panel.add(contrasenaLabel);

        JPasswordField contrasenaText = new JPasswordField(20);
        contrasenaText.setBounds(110, 60, 160, 25);
        panel.add(contrasenaText);

        final AtomicBoolean autenticado = new AtomicBoolean(autenticar("", "")); // Llamada inicial para actualizar autenticado
        JButton             botonEntrar = new JButton("Entrar");
        botonEntrar.setBounds(120, 100, 80, 30);
        botonEntrar.addActionListener(event -> {
            String usuario = nombreUsuario.getText();
            String contrasena = new String(contrasenaText.getPassword());
            autenticado.set(autenticar(usuario, contrasena)); // Actualiza el valor de autenticado
            if (autenticado.get()) {
                entradaCorrecta();
                frame.dispose(); // Cierra la ventana de login si el usuario se autentica correctamente
                new Pestanias();
            } else {
                entradaIncorrecta();
            }
        });
        panel.add(botonEntrar);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        return autenticado.get(); // Devuelve el valor actualizado de autenticado
    }


    public boolean autenticar(String usuario, String contrasena) {
        try {
            Connection con = getConnection();
            PreparedStatement stmt = con.prepareStatement("SELECT * FROM usuario WHERE NOMBRE = ? AND CONTRASENIA = ?");
            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);
            ResultSet rs = stmt.executeQuery();
            boolean autenticado = rs.next();
            rs.close();
            stmt.close();
            con.close();
            return autenticado;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void entradaCorrecta(){
        JOptionPane.showMessageDialog(null, "Bienvenido!");
    }
    public void entradaIncorrecta(){
        JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos. Por favor, inténtelo de nuevo.");
    }
}