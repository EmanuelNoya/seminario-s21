package conexionDB;

import modelo.InterruptorTermomagnetico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InterruptorTermomagneticoDAO {

    public InterruptorTermomagnetico save(InterruptorTermomagnetico it) throws Exception {
        String sql = "INSERT INTO InterruptorTermomagnetico(descripcion, amperaje, precio) VALUES (?, ?, ?)";
        try {
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
        } catch (Exception e) {
            System.err.println("InterruptorTermomagneticoDAO.save: error de BD: " + e.getMessage());
            throw new Exception("No se pudo guardar el interruptor termomagnético. Compruebe la conexión a la base de datos.", e);
        }
    }

    public InterruptorTermomagnetico update(InterruptorTermomagnetico it) throws Exception {
        if (it.getId() == null) throw new IllegalArgumentException("InterruptorTermomagnetico must have id to update");
        String sql = "UPDATE InterruptorTermomagnetico SET descripcion = ?, amperaje = ?, precio = ? WHERE id = ?";
        try {
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setString(1, it.getDescripcion());
                ps.setInt(2, it.getAmperaje());
                ps.setDouble(3, it.getPrecio());
                ps.setInt(4, it.getId());
                ps.executeUpdate();
                return it;
            }
        } catch (Exception e) {
            System.err.println("InterruptorTermomagneticoDAO.update: error de BD: " + e.getMessage());
            throw new Exception("No se pudo actualizar el interruptor termomagnético. Compruebe la conexión a la base de datos.", e);
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM InterruptorTermomagnetico WHERE id = ?";
        try {
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("InterruptorTermomagneticoDAO.delete: error de BD: " + e.getMessage());
            throw new Exception("No se pudo eliminar el interruptor termomagnético. Compruebe la conexión a la base de datos.", e);
        }
    }

    public List<InterruptorTermomagnetico> findAll() throws Exception {
        String sql = "SELECT id, descripcion, amperaje, precio FROM InterruptorTermomagnetico";
        List<InterruptorTermomagnetico> res = new ArrayList<>();
        try {
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InterruptorTermomagnetico it = new InterruptorTermomagnetico(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getDouble("precio"));
                    res.add(it);
                }
            }
        } catch (Exception e) {
            System.err.println("InterruptorTermomagneticoDAO.findAll: error de BD: " + e.getMessage());
            return new ArrayList<>();
        }
        return res;
    }

    public InterruptorTermomagnetico findById(int id) throws Exception {
        String sql = "SELECT id, descripcion, amperaje, precio FROM InterruptorTermomagnetico WHERE id = ?";
        try {
            try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new InterruptorTermomagnetico(rs.getInt("id"), rs.getString("descripcion"), rs.getInt("amperaje"), rs.getDouble("precio"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("InterruptorTermomagneticoDAO.findById: error de BD: " + e.getMessage());
            return null;
        }
        return null;
    }
}
