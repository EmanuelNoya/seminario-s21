package modelo;

public class InterruptorTermomagnetico extends Interruptor {
    private String descripcion;

    public InterruptorTermomagnetico(String descripcion, int amperaje, double precio) {
        super(amperaje, precio);
        this.descripcion = descripcion;
    }

    @Override
    public boolean proteger(int cargaTotal) {
        return cargaTotal <= getAmperaje();
    }

    public String getDescripcion() {
        return descripcion;
    }
}
