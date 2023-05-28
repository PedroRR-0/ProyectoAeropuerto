package modelo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ExportarDatos {

    public void exportarUsuariosAXml(Connection con) {
        try {
            // Crear el documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Crear el elemento raíz
            Element rootElement = doc.createElement("usuarios");
            doc.appendChild(rootElement);

            // Obtener los datos de la base de datos
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM usuario");

            // Recorrer los resultados y crear los elementos XML correspondientes
            while (rs.next()) {
                String nombre = rs.getString("NOMBRE");
                String contrasenia = rs.getString("CONTRASENIA");

                Element usuarioElement = doc.createElement("usuario");

                Element nombreElement = doc.createElement("deno");
                nombreElement.appendChild(doc.createTextNode(nombre));
                usuarioElement.appendChild(nombreElement);

                Element contraseniaElement = doc.createElement("pass");
                contraseniaElement.appendChild(doc.createTextNode(contrasenia));
                usuarioElement.appendChild(contraseniaElement);

                rootElement.appendChild(usuarioElement);
            }

            // Guardar el documento XML en un archivo
            FileWriter         fileWriter         = new FileWriter("Usuarios.xml");
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer        transformer        = transformerFactory.newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource (doc), new StreamResult (fileWriter));
            fileWriter.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void exportarAvionesAJson(Connection con) {
        try {
            // Obtener los datos de la base de datos
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM aviones");

            // Crear una lista de objetos Avion
            List <Avion> aviones = new ArrayList <> ();
            while (rs.next()) {
                int idAvion = rs.getInt("idAvion");
                String matricula = rs.getString("matricula");
                String modelo = rs.getString("modelo");
                int numAsientos = rs.getInt("numAsientos");
                int estado = rs.getInt("estado");

                Avion avion = new Avion(idAvion, matricula, modelo, numAsientos, estado);
                aviones.add(avion);
            }

            // Crear el objeto JSON
            Gson   gson = new GsonBuilder ().setPrettyPrinting().create();
            String json = gson.toJson(aviones);

            // Guardar el JSON en un archivo
            FileWriter fileWriter = new FileWriter("Aviones.json");
            fileWriter.write(json);
            fileWriter.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
