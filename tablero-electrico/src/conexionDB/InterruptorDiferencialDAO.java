package conexionDB;

import modelo.InterruptorDiferencial;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterruptorDiferencialDAO {

    public InterruptorDiferencial save(InterruptorDiferencial id) throws Exception {
        String sql = "INSERT INTO InterruptorDiferencial(descripcion, amperaje, sensibilidad, precio) VALUES (?, ?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, id.getDescripcion());
            ps.setInt(2, id.getAmperaje());
            ps.setInt(3, id.getSensibilidad());
            ps.setDouble(4, id.getPrecio());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    id.setId(rs.getInt(1));
                }
            }
            return id;
        }
    }

    public InterruptorDiferencial update(InterruptorDiferencial id) throws Exception {
        if (id.getId() == null) throw new IllegalArgumentException("InterruptorDiferencial must have id to update");
        String sql = "UPDATE InterruptorDiferencial SET descripcion = ?, amperaje = ?, sensibilidad = ?, precio = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, id.getDescripcion());
            ps.setInt(2, id.getAmperaje());
            ps.setInt(3, id.getSensibilidad());
            ps.setDouble(4, id.getPrecio());
            ps.setInt(5, id.getId());
            ps.executeUpdate();
            return id;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM InterruptorDiferencial WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<InterruptorDiferencial> findAll() throws Exception {
        String sql = "SELECT id, descripcion, amperaje, sensibilidad, precio FROM InterruptorDiferencial";
        List<InterruptorDiferencial> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InterruptorDiferencial id = new InterruptorDiferencial(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getInt("sensibilidad"), rs.getDouble("precio"));
                res.add(id);
            }
        }
        return res;
    }

    public InterruptorDiferencial findById(int id) throws Exception {
        String sql = "SELECT id, descripcion, amperaje, sensibilidad, precio FROM InterruptorDiferencial WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InterruptorDiferencial(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getInt("sensibilidad"), rs.getDouble("precio"));
                }
            }
        }
        return null;
    }
}
