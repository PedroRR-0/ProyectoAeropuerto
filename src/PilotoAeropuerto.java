import javax.swing.*;
import java.sql.Connection;

public class PilotoAeropuerto {
    public static void main(String[] args) {
        Login metodosLogin = new Login();
        CargaDeDatos cd = new CargaDeDatos ();
        Connection con = metodosLogin.getConnection();
        cd.cargarUsuariosDesdeXml(con);
        metodosLogin.mostrar();
    }
}
