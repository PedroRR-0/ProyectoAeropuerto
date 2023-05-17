package controlador;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logomens {
    private PrintWriter logWriter;
    LocalDateTime fech = LocalDateTime.now();
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    // Convertir la fecha y hora a una cadena de texto
    String strFechaHora = fech.format(formato);
    public void escribirRegistro(String mensaje) {
        try {
            String LOG_FILE = "C:\\Users\\DAM\\Desktop\\Logomens.txt";
            logWriter = new PrintWriter(new FileWriter( LOG_FILE , true));
        } catch (IOException e) {
            e.printStackTrace();
        }
        logWriter.println(mensaje+" el "+strFechaHora);
        logWriter.flush(); // Asegurar que los datos se escriban en el archivo inmediatamente
        logWriter.close ();
    }
}
