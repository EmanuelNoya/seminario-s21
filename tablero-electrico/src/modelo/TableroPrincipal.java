package modelo;

import java.util.ArrayList;
import java.util.List;

public class TableroPrincipal {
    private String descripcion;
    private InterruptorDiferencial interruptorDiferencial;
    private InterruptorTermomagnetico interruptorPrincipal;
    private List<Circuito> circuitos;

    public TableroPrincipal(String descripcion, InterruptorDiferencial diferencial, InterruptorTermomagnetico principal) {
        this.descripcion = descripcion;
        this.interruptorDiferencial = diferencial;
        this.interruptorPrincipal = principal;
        this.circuitos = new ArrayList<>();
    }

    public void agregarCircuito(Circuito c) {
        circuitos.add(c);
    }

    public List<Circuito> getCircuitos() {
        return circuitos;
    }

    public boolean validarTablero() {
        int cargaTotal = circuitos.stream().mapToInt(Circuito::getCargaTotal).sum();
        return interruptorDiferencial.proteger(cargaTotal) && interruptorPrincipal.proteger(cargaTotal);
    }
}
