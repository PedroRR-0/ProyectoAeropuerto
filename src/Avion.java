public class Avion {
    private int idAvion;
    private String matricula;
    private String modelo;
    private int    asientos;
    private int    estado;
    public Avion ( int idAvion , String matricula , String modelo , int asientos , int estado ) {
        this.idAvion   = idAvion;
        this.matricula = matricula;
        this.modelo    = modelo;
        this.asientos  = asientos;
        this.estado    = estado;
    }

    public int getIdAvion ( ) {
        return idAvion;
    }

    public void setIdAvion ( int idAvion ) {
        this.idAvion = idAvion;
    }

    public String getMatricula ( ) {
        return matricula;
    }

    public void setMatricula ( String matricula ) {
        this.matricula = matricula;
    }

    public String getModelo ( ) {
        return modelo;
    }

    public void setModelo ( String modelo ) {
        this.modelo = modelo;
    }

    public int getAsientos ( ) {
        return asientos;
    }

    public void setAsientos ( int asientos ) {
        this.asientos = asientos;
    }

    public int getEstado ( ) {
        return estado;
    }

    public void setEstado ( int estado ) {
        this.estado = estado;
    }
    @Override
    public String toString() {
        return "Avion{" +
                "idAvion=" + idAvion +
                ", matricula='" + matricula + '\'' +
                ", modelo='" + modelo + '\'' +
                ", asientos=" + asientos +
                ", estado=" + estado +
                '}';
    }
}
