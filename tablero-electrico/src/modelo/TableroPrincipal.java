package modelo;

import java.util.ArrayList;
import java.util.List;

public class TableroPrincipal {
    private Integer id;
    private String descripcion;
    private InterruptorDiferencial interruptorDiferencial;
    private InterruptorTermomagnetico interruptorPrincipal;
    private List<Circuito> circuitos;

    public TableroPrincipal(String descripcion, InterruptorDiferencial diferencial, InterruptorTermomagnetico principal) {
        this(null, descripcion, diferencial, principal);
    }

    public TableroPrincipal(Integer id, String descripcion, InterruptorDiferencial diferencial, InterruptorTermomagnetico principal) {
        this.id = id;
        this.descripcion = descripcion;
        this.interruptorDiferencial = diferencial;
        this.interruptorPrincipal = principal;
        this.circuitos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public InterruptorDiferencial getInterruptorDiferencial() {
        return interruptorDiferencial;
    }

    public void setInterruptorDiferencial(InterruptorDiferencial interruptorDiferencial) {
        this.interruptorDiferencial = interruptorDiferencial;
    }

    public InterruptorTermomagnetico getInterruptorPrincipal() {
        return interruptorPrincipal;
    }

    public void setInterruptorPrincipal(InterruptorTermomagnetico interruptorPrincipal) {
        this.interruptorPrincipal = interruptorPrincipal;
    }

    public void agregarCircuito(Circuito c) {
        circuitos.add(c);
    }

    public List<Circuito> getCircuitos() {
        return circuitos;
    }

    public void setCircuitos(List<Circuito> circuitos) {
        this.circuitos = circuitos;
    }

    public boolean validarTablero() {
        int cargaTotal = circuitos.stream().mapToInt(Circuito::getCargaTotal).sum();
        return interruptorDiferencial.proteger(cargaTotal) && interruptorPrincipal.proteger(cargaTotal);
    }
}
