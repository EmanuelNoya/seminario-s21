package controlador;

import modelo.Circuito;
import modelo.InterruptorTermomagnetico;
import conexionDB.CircuitoDAO;
import java.util.List;

public class CircuitoController {
    private CircuitoDAO dao = new CircuitoDAO();

    public Circuito create(String tipo, int calibreMaximo, int cantidadBocas, InterruptorTermomagnetico interruptor) throws Exception {
        Circuito c = new Circuito(tipo, calibreMaximo, cantidadBocas, interruptor);
        return dao.save(c);
    }

    public Circuito update(Circuito c) throws Exception {
        return dao.update(c);
    }

    public boolean delete(int id) throws Exception {
        return dao.delete(id);
    }

    public List<Circuito> listAll() throws Exception {
        return dao.findAll();
    }

    public Circuito findById(int id) throws Exception {
        return dao.findById(id);
    }

    public List<Circuito> findByTableroId(int tableId) throws Exception {
        return dao.findByTableroId(tableId);
    }
}
