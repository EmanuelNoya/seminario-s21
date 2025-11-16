package controlador;

import modelo.TableroPrincipal;
import modelo.InterruptorDiferencial;
import modelo.InterruptorTermomagnetico;
import conexionDB.TableroPrincipalDAO;
import java.util.List;

public class TableroPrincipalController {
    private TableroPrincipalDAO dao = new TableroPrincipalDAO();

    public TableroPrincipal create(String descripcion, InterruptorDiferencial diferencial, InterruptorTermomagnetico principal) throws Exception {
        TableroPrincipal tp = new TableroPrincipal(descripcion, diferencial, principal);
        return dao.save(tp);
    }

    public TableroPrincipal update(TableroPrincipal tp) throws Exception {
        return dao.update(tp);
    }

    public boolean delete(int id) throws Exception {
        return dao.delete(id);
    }

    public List<TableroPrincipal> listAll() throws Exception {
        return dao.findAll();
    }

    public TableroPrincipal findById(int id) throws Exception {
        return dao.findById(id);
    }
}
