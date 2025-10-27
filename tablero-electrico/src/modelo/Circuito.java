package modelo;

import java.util.ArrayList;
import java.util.List;

public class Circuito {
    private String tipo; // "IUG", "TUG", "TUE"
    private int calibreMaximo;
    private int cantidadBocas;
    private InterruptorTermomagnetico interruptor;
    private List<Artefacto> artefactos;

    public Circuito(String tipo, int calibreMaximo, int cantidadBocas, InterruptorTermomagnetico interruptor) {
        this.tipo = tipo;
        this.calibreMaximo = calibreMaximo;
        this.cantidadBocas = cantidadBocas;
        this.interruptor = interruptor;
        this.artefactos = new ArrayList<>();
    }

    public void agregarArtefacto(Artefacto a) {
        artefactos.add(a);
    }

    public List<Artefacto> getArtefactos() {
        return artefactos;
    }

    public InterruptorTermomagnetico getInterruptor() {
        return interruptor;
    }

    public int getCargaTotal() {
        return (artefactos.stream().mapToInt(Artefacto::getWattage).sum())/220; // Convertir watts a amperios (asumiendo 220V)
    }

    public String getTipo() {
        return tipo;
    }
}
