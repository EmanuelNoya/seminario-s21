package modelo;

public class InterruptorDiferencial extends Interruptor {
    private int sensibilidad;
    private String descripcion;

    public InterruptorDiferencial(String descripcion, int amperaje, int sensibilidad, double precio) {
        super(amperaje, precio);
        this.sensibilidad = sensibilidad;
        this.descripcion = descripcion;
    }

    @Override
    public boolean proteger(int cargaTotal) {
        return cargaTotal <= getAmperaje(); // simplificado
    }

    public int getSensibilidad() {
        return sensibilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
