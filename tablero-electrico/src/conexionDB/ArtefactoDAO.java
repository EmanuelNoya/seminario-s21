package conexionDB;

import modelo.Artefacto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArtefactoDAO {

    public Artefacto save(Artefacto a) throws Exception {
        String sql = "INSERT INTO Artefacto(nombre, wattage, tipo) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, a.getNombre());
            ps.setInt(2, a.getWattage());
            ps.setString(3, a.getTipo());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    a.setId(rs.getInt(1));
                }
            }
            return a;
        }
    }

    public Artefacto update(Artefacto a) throws Exception {
        if (a.getId() == null) throw new IllegalArgumentException("Artefacto debe tener un ID para ser actualizado");
        String sql = "UPDATE Artefacto SET nombre = ?, wattage = ?, tipo = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, a.getNombre());
            ps.setInt(2, a.getWattage());
            ps.setString(3, a.getTipo());
            ps.setInt(4, a.getId());
            ps.executeUpdate();
            return a;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM Artefacto WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<Artefacto> findAll() throws Exception {
        String sql = "SELECT id, nombre, wattage, tipo FROM Artefacto";
        List<Artefacto> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Artefacto a = new Artefacto(rs.getInt("id"), rs.getString("nombre"), rs.getInt("wattage"), rs.getString("tipo"));
                res.add(a);
            }
        }
        return res;
    }

    public Artefacto findById(int id) throws Exception {
        String sql = "SELECT id, nombre, wattage, tipo FROM Artefacto WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Artefacto(rs.getInt("id"), rs.getString("nombre"), rs.getInt("wattage"), rs.getString("tipo"));
                }
            }
        }
        return null;
    }
}
