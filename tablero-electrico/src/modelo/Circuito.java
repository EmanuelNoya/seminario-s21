package modelo;

import java.util.ArrayList;
import java.util.List;

public class Circuito {
    private Integer id;
    private String tipo; // "IUG", "TUG", "TUE"
    private int calibreMaximo;
    private int cantidadBocas;
    private InterruptorTermomagnetico interruptor;
    private List<Artefacto> artefactos;

    public Circuito(String tipo, int calibreMaximo, int cantidadBocas, InterruptorTermomagnetico interruptor) {
        this(null, tipo, calibreMaximo, cantidadBocas, interruptor);
    }

    public Circuito(Integer id, String tipo, int calibreMaximo, int cantidadBocas, InterruptorTermomagnetico interruptor) {
        this.id = id;
        this.tipo = tipo;
        this.calibreMaximo = calibreMaximo;
        this.cantidadBocas = cantidadBocas;
        this.interruptor = interruptor;
        this.artefactos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void agregarArtefacto(Artefacto a) {
        artefactos.add(a);
    }

    public List<Artefacto> getArtefactos() {
        return artefactos;
    }

    public void setArtefactos(List<Artefacto> artefactos) {
        this.artefactos = artefactos;
    }

    public InterruptorTermomagnetico getInterruptor() {
        return interruptor;
    }

    public void setInterruptor(InterruptorTermomagnetico interruptor) {
        this.interruptor = interruptor;
    }

    public int getCargaTotal() {
        return (artefactos.stream().mapToInt(Artefacto::getWattage).sum())/220; // Convertir watts a amperios (asumiendo 220V)
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCalibreMaximo() {
        return calibreMaximo;
    }

    public void setCalibreMaximo(int calibreMaximo) {
        this.calibreMaximo = calibreMaximo;
    }

    public int getCantidadBocas() {
        return cantidadBocas;
    }

    public void setCantidadBocas(int cantidadBocas) {
        this.cantidadBocas = cantidadBocas;
    }
}
