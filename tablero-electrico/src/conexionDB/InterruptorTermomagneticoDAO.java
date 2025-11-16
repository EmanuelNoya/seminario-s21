package conexionDB;

import modelo.InterruptorTermomagnetico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterruptorTermomagneticoDAO {

    public InterruptorTermomagnetico save(InterruptorTermomagnetico it) throws Exception {
        String sql = "INSERT INTO InterruptorTermomagnetico(descripcion, amperaje, precio) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, it.getDescripcion());
            ps.setInt(2, it.getAmperaje());
            ps.setDouble(3, it.getPrecio());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    it.setId(rs.getInt(1));
                }
            }
            return it;
        }
    }

    public InterruptorTermomagnetico update(InterruptorTermomagnetico it) throws Exception {
        if (it.getId() == null) throw new IllegalArgumentException("InterruptorTermomagnetico must have id to update");
        String sql = "UPDATE InterruptorTermomagnetico SET descripcion = ?, amperaje = ?, precio = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, it.getDescripcion());
            ps.setInt(2, it.getAmperaje());
            ps.setDouble(3, it.getPrecio());
            ps.setInt(4, it.getId());
            ps.executeUpdate();
            return it;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM InterruptorTermomagnetico WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<InterruptorTermomagnetico> findAll() throws Exception {
        String sql = "SELECT id, descripcion, amperaje, precio FROM InterruptorTermomagnetico";
        List<InterruptorTermomagnetico> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                InterruptorTermomagnetico it = new InterruptorTermomagnetico(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getDouble("precio"));
                res.add(it);
            }
        }
        return res;
    }

    public InterruptorTermomagnetico findById(int id) throws Exception {
        String sql = "SELECT id, descripcion, amperaje, precio FROM InterruptorTermomagnetico WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InterruptorTermomagnetico(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getDouble("precio"));
                }
            }
        }
        return null;
    }
}
