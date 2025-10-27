package modelo;

public class Artefacto {
    private String nombre;
    private int wattage;
    private String tipo; // "Iluminacion" o "Aparato"

    public Artefacto(String nombre, int wattage, String tipo) {
        this.nombre = nombre;
        this.wattage = wattage;
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getWattage() {
        return wattage;
    }

    public String getTipo() {
        return tipo;
    }
}
