import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.google.gson.Gson;

import java.util.List;
public class CargaDeDatos {
    public void cargarUsuariosDesdeXml( Connection con) {
        try {
            // Cargar el archivo XML
            File                   archivoXml = new File("C:\\Users\\lhuji\\Desktop\\ProyectoAeropuerto\\Recursos\\UsuariosHoy.xml");
            DocumentBuilderFactory factory    = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder    = factory.newDocumentBuilder();
            Document               doc        = builder.parse(archivoXml);

            // Obtener la lista de usuarios del archivo XML
            NodeList listaUsuarios = doc.getElementsByTagName("usuario");

            // Recorrer la lista de usuarios y agregarlos a la base de datos
            for (int i = 0; i < listaUsuarios.getLength(); i++) {
                Node nodoUsuario = listaUsuarios.item(i);
                if (nodoUsuario.getNodeType() == Node.ELEMENT_NODE) {
                    Element           elementoUsuario = (Element) nodoUsuario;
                    String            usuario         = elementoUsuario.getElementsByTagName("deno").item(0).getTextContent();
                    String            contrasenia     = elementoUsuario.getElementsByTagName("pass").item(0).getTextContent();
                    PreparedStatement stmt            = con.prepareStatement("INSERT INTO `proy3te4`.`usuario` (`NOMBRE`, `CONTRASENIA`) VALUES (?, ?) ON DUPLICATE KEY UPDATE NOMBRE = NOMBRE;");
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

    public void cargaAvionesDesdeJson(Connection con) {
        try {
            // Cargar el archivo JSON
            BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\lhuji\\Desktop\\ProyectoAeropuerto\\Recursos\\Aviones.json"));

            // Crear una instancia de Gson
            Gson gson = new Gson();

            // Deserializar el JSON a una lista de objetos de la clase Avion
            JsonObject  jsonObject = gson.fromJson(reader, JsonObject.class);
            JsonArray   jsonArray  = jsonObject.getAsJsonObject("aviones").getAsJsonArray("avion");
            List<Avion> aviones    = gson.fromJson(jsonArray, new TypeToken <List<Avion>> (){}.getType());

            // Insertar los objetos en la base de datos
            for (Avion avion : aviones) {
                PreparedStatement stmt = con.prepareStatement("INSERT INTO `aviones` (`idAvion`, `matricula`, `modelo`, `numAsientos`, `estado`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE idAvion = idAvion;");
                stmt.setInt(1, avion.getIdAvion());
                stmt.setString(2, avion.getMatricula());
                stmt.setString(3, avion.getModelo());
                stmt.setInt(4, avion.getAsientos());
                stmt.setInt(5, avion.getEstado());
                stmt.executeUpdate();
            }

            System.out.println("Aviones cargados desde el archivo JSON");
            System.out.println (aviones);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
