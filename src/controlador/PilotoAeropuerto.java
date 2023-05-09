package controlador;

import controlador.Login;
import modelo.CargaDeDatos;

import java.sql.Connection;
import java.sql.SQLException;


public class PilotoAeropuerto {
    public static void main(String[] args) throws SQLException {
        Login metodosLogin = new Login ();
        CargaDeDatos Cargainicial           = new CargaDeDatos ();
        Connection   Conexioninicial          = metodosLogin.getConnection();
        Cargainicial.cargarUsuariosDesdeXml(Conexioninicial);
        Cargainicial.cargaAvionesDesdeJson (Conexioninicial);
        Cargainicial.cargaTrayectosDesdeDat ( Conexioninicial );
        metodosLogin.mostrar();
    }
}
