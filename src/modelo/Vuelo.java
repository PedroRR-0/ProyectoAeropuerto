package modelo;

public class Vuelo {
    private int id;

    private String destino;
    private String origen;
    private String fecha;
    private String hora;

    public Vuelo(int id, String destino, String origen, String fecha, String hora){
        this.setDestino(destino);
        this.setOrigen(origen);
        this.setFecha(fecha);
        this.setHora(hora);
        this.id = id;
    }

    public String toString() {
        return getOrigen() + "-" + getDestino();
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

