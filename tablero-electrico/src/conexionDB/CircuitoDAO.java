package conexionDB;

import modelo.Circuito;
import modelo.InterruptorTermomagnetico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitoDAO {
    private InterruptorTermomagneticoDAO interruptorDAO = new InterruptorTermomagneticoDAO();

    public Circuito save(Circuito c) throws Exception {
        String sql = "INSERT INTO Circuito(tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, c.getTipo());
            ps.setInt(2, c.getCalibreMaximo());
            ps.setInt(3, c.getCantidadBocas());
            ps.setNull(4, java.sql.Types.INTEGER); // idTablero se asigna cuando se vincula al tablero
            if (c.getInterruptor() != null && c.getInterruptor().getId() != null) {
                ps.setInt(5, c.getInterruptor().getId());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    c.setId(rs.getInt(1));
                }
            }
            return c;
        }
    }

    public Circuito update(Circuito c) throws Exception {
        if (c.getId() == null) throw new IllegalArgumentException("Circuito must have id to update");
        String sql = "UPDATE Circuito SET tipo = ?, calibreMaximo = ?, cantidadBocas = ?, idInterruptorTermomagnetico = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getTipo());
            ps.setInt(2, c.getCalibreMaximo());
            ps.setInt(3, c.getCantidadBocas());
            if (c.getInterruptor() != null && c.getInterruptor().getId() != null) {
                ps.setInt(4, c.getInterruptor().getId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            ps.setInt(5, c.getId());
            ps.executeUpdate();
            return c;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM Circuito WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<Circuito> findAll() throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idInterruptorTermomagnetico FROM Circuito";
        List<Circuito> res = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Integer interruptorId = rs.getInt("idInterruptorTermomagnetico");
                if (rs.wasNull()) interruptorId = null;

                InterruptorTermomagnetico interruptor = null;
                if (interruptorId != null) {
                    interruptor = interruptorDAO.findById(interruptorId);
                }

                Circuito c = new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor);
                res.add(c);
            }
        }
        return res;
    }

    public Circuito findById(int id) throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idInterruptorTermomagnetico FROM Circuito WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Integer interruptorId = rs.getInt("idInterruptorTermomagnetico");
                    if (rs.wasNull()) interruptorId = null;

                    InterruptorTermomagnetico interruptor = null;
                    if (interruptorId != null) {
                        interruptor = interruptorDAO.findById(interruptorId);
                    }

                    return new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor);
                }
            }
        }
        return null;
    }

    public List<Circuito> findByTableroId(int tableId) throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idInterruptorTermomagnetico FROM Circuito WHERE idTablero = ?";
        List<Circuito> res = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, tableId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer interruptorId = rs.getInt("idInterruptorTermomagnetico");
                    if (rs.wasNull()) interruptorId = null;

                    InterruptorTermomagnetico interruptor = null;
                    if (interruptorId != null) {
                        interruptor = interruptorDAO.findById(interruptorId);
                    }

                    Circuito c = new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor);
                    res.add(c);
                }
            }
        }
        return res;
    }
}
