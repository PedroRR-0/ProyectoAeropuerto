import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class Autenticacion extends JFrame implements ActionListener {
    private JTextField usuarioField;
    private JPasswordField passwordField;
    private JButton ingresarButton;

    public void Autenticaciona() {
        // Configurar la ventana
        setTitle("Autenticación");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Crear los componentes
        JLabel usuarioLabel = new JLabel("Usuario:");
        usuarioField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField(20);
        ingresarButton = new JButton("Ingresar");
        ingresarButton.addActionListener(this);

        // Agregar los componentes a la ventana
        JPanel panel = new JPanel();
        panel.add(usuarioLabel);
        panel.add(usuarioField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(ingresarButton);
        add(panel);

        // Conectar a la base de datos
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://localhost/aeropuerto", "usuario", "contraseña");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        // Verificar la autenticación en la base de datos
        String usuario = usuarioField.getText();
        char[] password = passwordField.getPassword();
        // ...
    }
}
