package controlador;

import modelo.Artefacto;
import conexionDB.ArtefactoPorCircuitoDAO;
import java.util.List;

public class ArtefactoPorCircuitoController {
    private ArtefactoPorCircuitoDAO dao = new ArtefactoPorCircuitoDAO();

    public void asociarArtefactoACircuito(int idCircuito, int idArtefacto) throws Exception {
        dao.asociarArtefactoACircuito(idCircuito, idArtefacto);
    }

    public boolean desasociarArtefactoDelCircuito(int idCircuito, int idArtefacto) throws Exception {
        return dao.desasociarArtefactoDelCircuito(idCircuito, idArtefacto);
    }

    public List<Artefacto> getArtefactoPorCircuito(int idCircuito) throws Exception {
        return dao.getArtefactoPorCircuito(idCircuito);
    }

    public List<Integer> getCircuitosPorArtefactoo(int idArtefacto) throws Exception {
        return dao.getCircuitosPorArtefactoo(idArtefacto);
    }

    public boolean deleteArtefactosDelCircuito(int idCircuito) throws Exception {
        return dao.deleteArtefactosDelCircuito(idCircuito);
    }
}
