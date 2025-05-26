package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Visita;
import backsaim.example.saim.db.util.SingletonDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VisitaDAL implements IDAL<Visita> {

    @Override
    public boolean gravar(Visita visita) {
        return agendarVisita(visita);
    }

    public boolean agendarVisita(Visita visita) {
        String sql = """
            INSERT INTO visita(VisiNome, VisiCPF, VisiData_Entrada, VisiData_Saida)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, visita.getVisinome());
            stmt.setString(2, visita.getVisicpf());
            stmt.setDate(3, Date.valueOf(visita.getVisidata_entrada()));
            if (visita.getVisidata_saida() != null)
                stmt.setDate(4, Date.valueOf(visita.getVisidata_saida()));
            else
                stmt.setNull(4, Types.DATE);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cpfExiste(String cpf) {
        String sql = "SELECT 1 FROM visita WHERE VisiCPF = ? LIMIT 1";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean alterar(Visita visita) {
        String sql = """
            UPDATE visita SET VisiNome = ?, VisiCPF = ?, VisiData_Entrada = ?, VisiData_Saida = ?
            WHERE VisiCod = ?
        """;
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, visita.getVisinome());
            stmt.setString(2, visita.getVisicpf());
            stmt.setDate(3, Date.valueOf(visita.getVisidata_entrada()));
            if (visita.getVisidata_saida() != null)
                stmt.setDate(4, Date.valueOf(visita.getVisidata_saida()));
            else
                stmt.setNull(4, Types.DATE);
            stmt.setInt(5, visita.getVisicod());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean apagar(Visita visita) {
        String sql = "DELETE FROM visita WHERE VisiCod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, visita.getVisicod());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Visita get(int id) {
        String sql = "SELECT * FROM visita WHERE VisiCod = ?";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Visita(
                        rs.getInt("VisiCod"),
                        rs.getString("VisiNome"),
                        rs.getString("VisiCPF"),
                        rs.getDate("VisiData_Entrada").toLocalDate(),
                        rs.getDate("VisiData_Saida") != null ? rs.getDate("VisiData_Saida").toLocalDate() : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Visita> get(String filtro) {
        List<Visita> visitas = new ArrayList<>();
        String sql = "SELECT * FROM visita";
        if (!filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try (Statement stmt = SingletonDB.getConexao().getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                visitas.add(new Visita(
                        rs.getInt("VisiCod"),
                        rs.getString("VisiNome"),
                        rs.getString("VisiCPF"),
                        rs.getDate("VisiData_Entrada").toLocalDate(),
                        rs.getDate("VisiData_Saida") != null ? rs.getDate("VisiData_Saida").toLocalDate() : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return visitas;
    }
}
