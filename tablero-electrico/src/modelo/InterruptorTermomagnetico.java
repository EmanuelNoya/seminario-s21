package modelo;

public class InterruptorTermomagnetico extends Interruptor {
    private String descripcion;

    public InterruptorTermomagnetico(String descripcion, int amperaje, double precio) {
        super(amperaje, precio);
        this.descripcion = descripcion;
    }

    public InterruptorTermomagnetico(Integer id, String descripcion, int amperaje, double precio) {
        super(id, amperaje, precio);
        this.descripcion = descripcion;
    }

    @Override
    public boolean proteger(int cargaTotal) {
        return cargaTotal <= getAmperaje();
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
