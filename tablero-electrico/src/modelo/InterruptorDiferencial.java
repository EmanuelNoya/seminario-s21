package modelo;

public class InterruptorDiferencial extends Interruptor {
    private int sensibilidad;
    private String descripcion;

    public InterruptorDiferencial(String descripcion, int amperaje, int sensibilidad, double precio) {
        super(amperaje, precio);
        this.sensibilidad = sensibilidad;
        this.descripcion = descripcion;
    }

    public InterruptorDiferencial(Integer id, String descripcion, int amperaje, int sensibilidad, double precio) {
        super(id, amperaje, precio);
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

    public void setSensibilidad(int sensibilidad) {
        this.sensibilidad = sensibilidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
