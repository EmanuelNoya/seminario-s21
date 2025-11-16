package controlador;

import modelo.InterruptorDiferencial;
import conexionDB.InterruptorDiferencialDAO;
import java.util.List;

public class InterruptorDiferencialController {
    private InterruptorDiferencialDAO dao = new InterruptorDiferencialDAO();

    public InterruptorDiferencial create(String descripcion, int amperaje, int sensibilidad, double precio) throws Exception {
        InterruptorDiferencial id = new InterruptorDiferencial(descripcion, amperaje, sensibilidad, precio);
        return dao.save(id);
    }

    public InterruptorDiferencial update(InterruptorDiferencial id) throws Exception {
        return dao.update(id);
    }

    public boolean delete(int id) throws Exception {
        return dao.delete(id);
    }

    public List<InterruptorDiferencial> listAll() throws Exception {
        return dao.findAll();
    }

    public InterruptorDiferencial findById(int id) throws Exception {
        return dao.findById(id);
    }
}
