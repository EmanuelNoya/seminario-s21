package modelo;

public abstract class Interruptor {
    private int amperaje;
    private double precio;

    public Interruptor(int amperaje, double precio) {
        this.amperaje = amperaje;
        this.precio = precio;
    }

    public int getAmperaje() {
        return amperaje;
    }

    public double getPrecio() {
        return precio;
    }

    public abstract boolean proteger(int cargaTotal);
}
