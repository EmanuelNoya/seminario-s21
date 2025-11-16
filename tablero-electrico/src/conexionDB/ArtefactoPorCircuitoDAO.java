package conexionDB;

import modelo.Artefacto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtefactoPorCircuitoDAO {
    //private ArtefactoDAO artefactoDAO = new ArtefactoDAO();

    /**
     * Vincula un artefacto a un circuito
     */
    public void asociarArtefactoACircuito(int idCircuito, int idArtefacto) throws Exception {
        String sql = "INSERT INTO ArtefactoPorCircuito(idCircuito, idArtefacto) VALUES (?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCircuito);
            ps.setInt(2, idArtefacto);
            ps.executeUpdate();
        }
    }

    /**
     * Desvincula un artefacto de un circuito
     */
    public boolean desasociarArtefactoDelCircuito(int idCircuito, int idArtefacto) throws Exception {
        String sql = "DELETE FROM ArtefactoPorCircuito WHERE idCircuito = ? AND idArtefacto = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCircuito);
            ps.setInt(2, idArtefacto);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    /**
     * Obtiene todos los artefactos de un circuito
     */
    public List<Artefacto> getArtefactoPorCircuito(int idCircuito) throws Exception {
        String sql = "SELECT a.id, a.nombre, a.wattage, a.tipo FROM Artefacto a " +
                     "INNER JOIN ArtefactoPorCircuito ac ON a.id = ac.idArtefacto " +
                     "WHERE ac.idCircuito = ?";
        List<Artefacto> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCircuito);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Artefacto a = new Artefacto(rs.getInt("id"), rs.getString("nombre"), rs.getInt("wattage"), rs.getString("tipo"));
                    res.add(a);
                }
            }
        }
        return res;
    }

    /**
     * Obtiene todos los circuitos de un artefacto
     */
    public List<Integer> getCircuitosPorArtefactoo(int idArtefacto) throws Exception {
        String sql = "SELECT idCircuito FROM ArtefactoPorCircuito WHERE idArtefacto = ?";
        List<Integer> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idArtefacto);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    res.add(rs.getInt("idCircuito"));
                }
            }
        }
        return res;
    }

    /**
     * Elimina todos los artefactos de un circuito
     */
    public boolean deleteArtefactosDelCircuito(int idCircuito) throws Exception {
        String sql = "DELETE FROM ArtefactoPorCircuito WHERE idCircuito = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, idCircuito);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }
}
