package modelo;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import controlador.Logomens;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.List;
public class CargaDeDatos {
    public void cargarUsuariosDesdeXml( Connection con) {
        try {
            // Cargar el archivo XML
            DocumentBuilderFactory factory    = DocumentBuilderFactory.newInstance();
            DocumentBuilder        builder    = factory.newDocumentBuilder();
            Document               doc        = builder.parse(getClass().getClassLoader().getResourceAsStream("UsuariosHoy.xml"));

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Logomens log = new Logomens ();
        log.escribirRegistro("Usuarios cargados desde el archivo XML");
    }
    public void cargaAvionesDesdeJson(Connection con) {
        try {
            // Cargar el archivo JSON
            InputStream strem = getClass().getClassLoader().getResourceAsStream("Aviones.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(strem));

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Logomens log = new Logomens ();
        log.escribirRegistro("Aviones cargados desde el archivo JSON");
    }
    public void cargaTrayectosDesdeDat(Connection con) throws SQLException {

        String filePath = "Trayectos.dat";
        try {
            // Extraer los datos del fichero binario
            DataInputStream dataInputStream = new DataInputStream(getClass().getClassLoader().getResourceAsStream(filePath));
            while (dataInputStream.available() > 0) {
                int number = dataInputStream.readInt();
                String word1 = new String(dataInputStream.readUTF().getBytes("ISO-8859-1"), "UTF-8");
                String word2 = new String(dataInputStream.readUTF().getBytes("ISO-8859-1"), "UTF-8");
                // Insertar datos en tabla trayectos
                PreparedStatement stmt = con.prepareStatement("INSERT INTO trayectos (idTrayecto, origen, destino) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE idTrayecto = idTrayecto");
                stmt.setInt(1, number);
                stmt.setString(2, word1);
                stmt.setString(3, word2);
                stmt.executeUpdate();
            }


            dataInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Logomens log = new Logomens ();
        log.escribirRegistro("Trayectos cargados desde el archivo DAT");
    }

}



