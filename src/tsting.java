import java.sql.*;
import java.lang.ClassNotFoundException;

public class tsting {

    public Connection getConnection(){
        try {
            //El método forName() carga el driver en el programa
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException cnfe) {
            System.out.println(cnfe.getMessage());
        }
        Connection con;
        try {
            con = DriverManager.getConnection("jdbc:mariadb://localhost:3306/Proy3TE4","root","123456");
            System.out.println("Tenemos conexión...");
            Statement encapsulaCons = con.createStatement();
            String consulta = "SELECT * FROM usuario";
            ResultSet resulCons = encapsulaCons.executeQuery(consulta);
            System.out.println("Cabecera del listado a imprimir");
            while (resulCons.next()) { //Recorre registro a registro el resultado obtenido
                System.out.println("Campo " + resulCons.getString("nombre"));
            }

            encapsulaCons.close();
            con.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return con;
    }

    public static void main(String[] args) {
        tsting t = new tsting();
        t.getConnection();
    }
}
