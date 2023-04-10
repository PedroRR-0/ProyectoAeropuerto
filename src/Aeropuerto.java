import javax.swing.*;

public class Aeropuerto {
    public static void main(String[] args) {
        // Inicializar la aplicación
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                // Crear la ventana principal
                JFrame frame = new JFrame("Aeropuerto");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 600);
                frame.setVisible(true);
                Autenticacion a = new Autenticacion ();
                a.Autenticaciona ();
            }
        });
    }
}
