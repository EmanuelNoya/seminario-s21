package modelo;

public abstract class Interruptor {
    private Integer id;
    private int amperaje;
    private double precio;

    public Interruptor(int amperaje, double precio) {
        this(null, amperaje, precio);
    }

    public Interruptor(Integer id, int amperaje, double precio) {
        this.id = id;
        this.amperaje = amperaje;
        this.precio = precio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmperaje() {
        return amperaje;
    }

    public void setAmperaje(int amperaje) {
        this.amperaje = amperaje;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public abstract boolean proteger(int cargaTotal);
}
