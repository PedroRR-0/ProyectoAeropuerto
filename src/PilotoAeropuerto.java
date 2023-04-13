import javax.swing.*;
import java.sql.Connection;

public class PilotoAeropuerto {
    public static void main(String[] args) {
        Login metodosLogin = new Login();
        Connection con = metodosLogin.getConnection();
        metodosLogin.cargarUsuariosDesdeXml(con);
        metodosLogin.mostrar();
    }
}
