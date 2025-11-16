package controlador;

import modelo.InterruptorTermomagnetico;
import conexionDB.InterruptorTermomagneticoDAO;
import java.util.List;

public class InterruptorTermomagneticoController {
    private InterruptorTermomagneticoDAO dao = new InterruptorTermomagneticoDAO();

    public InterruptorTermomagnetico create(String descripcion, int amperaje, double precio) throws Exception {
        InterruptorTermomagnetico it = new InterruptorTermomagnetico(descripcion, amperaje, precio);
        return dao.save(it);
    }

    public InterruptorTermomagnetico update(InterruptorTermomagnetico it) throws Exception {
        return dao.update(it);
    }

    public boolean delete(int id) throws Exception {
        return dao.delete(id);
    }

    public List<InterruptorTermomagnetico> listAll() throws Exception {
        return dao.findAll();
    }

    public InterruptorTermomagnetico findById(int id) throws Exception {
        return dao.findById(id);
    }
}
