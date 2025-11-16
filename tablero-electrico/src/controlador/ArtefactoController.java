package controlador;

import modelo.Artefacto;
import conexionDB.ArtefactoDAO;

import java.util.List;

public class ArtefactoController {
    private ArtefactoDAO dao = new ArtefactoDAO();

    public Artefacto create(String nombre, int watt, String tipo) throws Exception {
        Artefacto a = new Artefacto(nombre, watt, tipo);
        return dao.save(a);
    }

    public Artefacto update(Artefacto a) throws Exception {
        return dao.update(a);
    }

    public boolean delete(int id) throws Exception {
        return dao.delete(id);
    }

    public List<Artefacto> listAll() throws Exception {
        return dao.findAll();
    }

    public Artefacto findById(int id) throws Exception {
        return dao.findById(id);
    }
}
