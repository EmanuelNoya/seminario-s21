package modelo;

public class Artefacto {
    private Integer id; // puede ser nulo si no está persistido aún
    private String nombre;
    private int wattage;
    private String tipo; // "Iluminacion" o "Aparato"

    public Artefacto(String nombre, int wattage, String tipo) {
        this(null, nombre, wattage, tipo);
    }

    public Artefacto(Integer id, String nombre, int wattage, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.wattage = wattage;
        this.tipo = tipo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getWattage() {
        return wattage;
    }

    public void setWattage(int wattage) {
        this.wattage = wattage;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Artefacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", wattage=" + wattage +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
