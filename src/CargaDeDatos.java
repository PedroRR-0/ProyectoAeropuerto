import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
}
