package controlador;

import controlador.Login;
import modelo.CargaDeDatos;

import java.sql.Connection;


public class PilotoAeropuerto {
    public static void main(String[] args) {
        Login metodosLogin = new Login ();
        CargaDeDatos Cargainicial           = new CargaDeDatos ();
        Connection   Conexioninicial          = metodosLogin.getConnection();
        Cargainicial.cargarUsuariosDesdeXml(Conexioninicial);
        Cargainicial.cargaAvionesDesdeJson (Conexioninicial);
        metodosLogin.mostrar();
    }
}
