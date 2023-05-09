package modelo;

public class Miembro {
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String direccion;
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String categoria;

    public Miembro ( String nombre , String apellido1 , String apellido2 , String direccion , String fechaNacimiento , String telefono , String email , String categoria ) {
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
    }


    public String getNombre ( ) {
        return nombre;
    }

    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    public String getApellido1 ( ) {
        return apellido1;
    }

    public void setApellido1 ( String apellido1 ) {
        this.apellido1 = apellido1;
    }

    public String getApellido2 ( ) {
        return apellido2;
    }

    public void setApellido2 ( String apellido2 ) {
        this.apellido2 = apellido2;
    }

    public String getDireccion ( ) {
        return direccion;
    }

    public void setDireccion ( String direccion ) {
        this.direccion = direccion;
    }

    public String getFechaNacimiento ( ) {
        return fechaNacimiento;
    }

    public void setFechaNacimiento ( String fechaNacimiento ) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono ( ) {
        return telefono;
    }

    public void setTelefono ( String telefono ) {
        this.telefono = telefono;
    }

    public String getEmail ( ) {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getCategoria ( ) {
        return categoria;
    }

    public void setCategoria ( String categoria ) {
        this.categoria = categoria;
    }

    @Override
    public String toString ( ) {
        return "Miembro{" +
                "nombre='" + nombre + '\'' +
                ", apellido1='" + apellido1 + '\'' +
                ", apellido2='" + apellido2 + '\'' +
                ", direccion='" + direccion + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", categoria='" + categoria + '\'' +
                '}';
    }

}
