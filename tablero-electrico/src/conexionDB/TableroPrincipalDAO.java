package conexionDB;

import modelo.TableroPrincipal;
import modelo.InterruptorDiferencial;
import modelo.InterruptorTermomagnetico;
import modelo.Circuito;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TableroPrincipalDAO {
    private InterruptorDiferencialDAO interruptorDiferencialDAO = new InterruptorDiferencialDAO();
    private InterruptorTermomagneticoDAO interruptorTermomagneticoDAO = new InterruptorTermomagneticoDAO();
    private CircuitoDAO circuitoDAO = new CircuitoDAO();

    public TableroPrincipal save(TableroPrincipal tp) throws Exception {
        String sql = "INSERT INTO TableroPrincipal(descripcion, idInterruptorDiferencial, idInterruptorTermomagnetico) VALUES (?, ?, ?)";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, tp.getDescripcion());
            if (tp.getInterruptorDiferencial() != null && tp.getInterruptorDiferencial().getId() != null) {
                ps.setInt(2, tp.getInterruptorDiferencial().getId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (tp.getInterruptorPrincipal() != null && tp.getInterruptorPrincipal().getId() != null) {
                ps.setInt(3, tp.getInterruptorPrincipal().getId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    tp.setId(rs.getInt(1));
                }
            }
            return tp;
        }
    }

    public TableroPrincipal update(TableroPrincipal tp) throws Exception {
        if (tp.getId() == null) throw new IllegalArgumentException("TableroPrincipal must have id to update");
        String sql = "UPDATE TableroPrincipal SET descripcion = ?, idInterruptorDiferencial = ?, idInterruptorTermomagnetico = ? WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, tp.getDescripcion());
            if (tp.getInterruptorDiferencial() != null && tp.getInterruptorDiferencial().getId() != null) {
                ps.setInt(2, tp.getInterruptorDiferencial().getId());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (tp.getInterruptorPrincipal() != null && tp.getInterruptorPrincipal().getId() != null) {
                ps.setInt(3, tp.getInterruptorPrincipal().getId());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4, tp.getId());
            ps.executeUpdate();
            return tp;
        }
    }

    public boolean delete(int id) throws Exception {
        String sql = "DELETE FROM TableroPrincipal WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }
    }

    public List<TableroPrincipal> findAll() throws Exception {
        String sql = "SELECT id, descripcion, idInterruptorDiferencial, idInterruptorTermomagnetico FROM TableroPrincipal";
        List<TableroPrincipal> res = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                TableroPrincipal tp = reconstructTableroPrincipal(rs);
                res.add(tp);
            }
        }
        return res;
    }

    public TableroPrincipal findById(int id) throws Exception {
        String sql = "SELECT id, descripcion, idInterruptorDiferencial, idInterruptorTermomagnetico FROM TableroPrincipal WHERE id = ?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return reconstructTableroPrincipal(rs);
                }
            }
        }
        return null;
    }

    private TableroPrincipal reconstructTableroPrincipal(ResultSet rs) throws Exception {
        Integer idDiferencial = rs.getInt("idInterruptorDiferencial");
        if (rs.wasNull()) idDiferencial = null;

        Integer idTermomagnetico = rs.getInt("idInterruptorTermomagnetico");
        if (rs.wasNull()) idTermomagnetico = null;

        InterruptorDiferencial diferencial = null;
        if (idDiferencial != null) {
            diferencial = interruptorDiferencialDAO.findById(idDiferencial);
        }

        InterruptorTermomagnetico termomagnetico = null;
        if (idTermomagnetico != null) {
            termomagnetico = interruptorTermomagneticoDAO.findById(idTermomagnetico);
        }

        TableroPrincipal tp = new TableroPrincipal(rs.getInt("id"), rs.getString("descripcion"), diferencial, termomagnetico);

        // Cargar circuitos asociados
        List<Circuito> circuitos = circuitoDAO.findByTableroId(rs.getInt("id"));
        tp.setCircuitos(circuitos);

        return tp;
    }
}
