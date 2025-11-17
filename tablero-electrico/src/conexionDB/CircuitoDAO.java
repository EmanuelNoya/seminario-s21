package conexionDB;

import modelo.Circuito;
import modelo.InterruptorTermomagnetico;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitoDAO {
    private InterruptorTermomagneticoDAO interruptorDAO = new InterruptorTermomagneticoDAO();
    private ArtefactoPorCircuitoDAO artefactoPorCircuitoDAO = new ArtefactoPorCircuitoDAO();

    public Circuito save(Circuito c) throws Exception {
        String sql = "INSERT INTO Circuito(tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico) VALUES (?, ?, ?, ?, ?)";
        try {
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getTipo());
                ps.setInt(2, c.getCalibreMaximo());
                ps.setInt(3, c.getCantidadBocas());
                ps.setInt(4, c.getTableroId());
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
        } catch (Exception e) {
            System.err.println("CircuitoDAO.save: error de BD: " + e.getMessage());
            throw new Exception("No se pudo guardar el circuito. Compruebe la conexión a la base de datos.", e);
        }
    }

    public Circuito update(Circuito c) throws Exception {
        if (c.getId() == null) throw new IllegalArgumentException("Circuito must have id to update");
        String sql = "UPDATE Circuito SET tipo = ?, calibreMaximo = ?, cantidadBocas = ?, idInterruptorTermomagnetico = ? WHERE id = ?";
        try {
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
        } catch (Exception e) {
            System.err.println("CircuitoDAO.update: error de BD: " + e.getMessage());
            throw new Exception("No se pudo actualizar el circuito. Compruebe la conexión a la base de datos.", e);
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM Circuito WHERE id = ?";
        try {
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                int rows = ps.executeUpdate();
                return rows > 0;
            }
        } catch (Exception e) {
            System.err.println("CircuitoDAO.delete: error de BD: " + e.getMessage());
            throw new Exception("No se pudo eliminar el circuito. Compruebe la conexión a la base de datos.", e);
        }
    }

    public List<Circuito> findAll() throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico FROM Circuito";
        List<Circuito> res = new ArrayList<>();
        try {
            try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer interruptorId = rs.getInt("idInterruptorTermomagnetico");
                    if (rs.wasNull()) interruptorId = null;

                    InterruptorTermomagnetico interruptor = null;
                    if (interruptorId != null) {
                        interruptor = interruptorDAO.findById(interruptorId);
                    }

                    Circuito c = new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor, rs.getInt("idTablero"));
                    // Cargar artefactos asociados
                    c.setArtefactos(artefactoPorCircuitoDAO.getArtefactoPorCircuito(c.getId()));
                    res.add(c);
                }
            }
        } catch (Exception e) {
            System.err.println("CircuitoDAO.findAll: error de BD: " + e.getMessage());
            return new ArrayList<>();
        }
        return res;
    }

    public Circuito findById(int id) throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico FROM Circuito WHERE id = ?";
        try {
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

                        Circuito c = new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor, rs.getInt("idTablero"));
                        // Cargar artefactos asociados
                        c.setArtefactos(artefactoPorCircuitoDAO.getArtefactoPorCircuito(c.getId()));
                        return c;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("CircuitoDAO.findById: error de BD: " + e.getMessage());
            return null;
        }
        return null;
    }

    public List<Circuito> findByTableroId(int tableId) throws Exception {
        String sql = "SELECT id, tipo, calibreMaximo, cantidadBocas, idTablero, idInterruptorTermomagnetico FROM Circuito WHERE idTablero = ?";
        List<Circuito> res = new ArrayList<>();
        try {
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

                        Circuito c = new Circuito(rs.getInt("id"), rs.getString("tipo"), rs.getInt("calibreMaximo"), rs.getInt("cantidadBocas"), interruptor, rs.getInt("idTablero"));
                        // Cargar artefactos asociados
                        c.setArtefactos(artefactoPorCircuitoDAO.getArtefactoPorCircuito(c.getId()));
                        res.add(c);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("CircuitoDAO.findByTableroId: error de BD: " + e.getMessage());
            return new ArrayList<>();
        }
        return res;
    }
}
