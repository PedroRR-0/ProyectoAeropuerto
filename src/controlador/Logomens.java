package controlador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class Logomens {
    private PrintWriter logWriter;
    public void escribirRegistro(String mensaje) {
        try {
            String LOG_FILE = "recursos/Logomens.txt";
            logWriter = new PrintWriter(new FileWriter( LOG_FILE , true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logWriter.println(mensaje);
        logWriter.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
        logWriter.close ();
    }
}
