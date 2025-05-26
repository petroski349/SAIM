package backsaim.example.saim.db.dal;

import backsaim.example.saim.db.entidade.Visitante;
import backsaim.example.saim.db.util.SingletonDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VisitanteDAL implements IDAL<Visitante> {

    @Override
    public boolean gravar(Visitante entidade) {
        String sql = """
                INSERT INTO visitante (VisiNome, VisiCPF, VisiEmail, VisiTelefone, VisiEndereco, VisiCep, VisiData_Cadastro, VisiData_Nascimento)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?);
                """;
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, entidade.getVisiNome());
            stmt.setString(2, entidade.getVisiCPF());
            stmt.setString(3, entidade.getVisiEmail());
            stmt.setString(4, entidade.getVisiTelefone());
            stmt.setString(5, entidade.getVisiEndereco());
            stmt.setString(6, entidade.getVisiCep());
            stmt.setDate(7, java.sql.Date.valueOf(entidade.getVisiDataCadastro()));
            stmt.setDate(8, java.sql.Date.valueOf(entidade.getVisiDataNascimento()));

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean alterar(Visitante entidade) {
        String sql = """
                UPDATE visitante SET
                    VisiNome = ?,
                    VisiCPF = ?,
                    VisiEmail = ?,
                    VisiTelefone = ?,
                    VisiEndereco = ?,
                    VisiCep = ?,
                    VisiData_Cadastro = ?,
                    VisiData_Nascimento = ?
                WHERE VisiCod = ?;
                """;
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, entidade.getVisiNome());
            stmt.setString(2, entidade.getVisiCPF());
            stmt.setString(3, entidade.getVisiEmail());
            stmt.setString(4, entidade.getVisiTelefone());
            stmt.setString(5, entidade.getVisiEndereco());
            stmt.setString(6, entidade.getVisiCep());
            stmt.setDate(7, java.sql.Date.valueOf(entidade.getVisiDataCadastro()));
            stmt.setDate(8, java.sql.Date.valueOf(entidade.getVisiDataNascimento()));
            stmt.setInt(9, entidade.getVisiCod());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean apagar(Visitante entidade) {
        String sql = "DELETE FROM visitante WHERE VisiCod = ?;";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, entidade.getVisiCod());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Visitante> listar() {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante ORDER BY VisiNome";
        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                visitantes.add(new Visitante(
                        rs.getInt("VisiCod"),
                        rs.getString("VisiNome"),
                        rs.getString("VisiCPF"),
                        rs.getString("VisiEmail"),
                        rs.getString("VisiTelefone"),
                        rs.getString("VisiEndereco"),
                        rs.getString("VisiCep"),
                        rs.getDate("VisiData_Cadastro").toLocalDate(),
                        rs.getDate("VisiData_Nascimento").toLocalDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitantes;
    }


    @Override
    public Visitante get(int id) {
        String sql = "SELECT * FROM visitante WHERE VisiCod = ?;";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Visitante(
                            rs.getInt("VisiCod"),
                            rs.getString("VisiNome"),
                            rs.getString("VisiCPF"),
                            rs.getString("VisiEmail"),
                            rs.getString("VisiTelefone"),
                            rs.getString("VisiEndereco"),
                            rs.getString("VisiCep"),
                            rs.getDate("VisiData_Cadastro").toLocalDate(),
                            rs.getDate("VisiData_Nascimento").toLocalDate()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean cpfjaExiste(String cpf) {
        String sql = "SELECT 1 FROM visitante WHERE VisiCPF = ? LIMIT 1;";
        try (PreparedStatement stmt = SingletonDB.getConexao().getConnection().prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Visitante> get(String filtro) {
        List<Visitante> visitantes = new ArrayList<>();
        String sql = "SELECT * FROM visitante";
        if (filtro != null && !filtro.isEmpty()) {
            sql += " WHERE " + filtro;
        }

        try (ResultSet rs = SingletonDB.getConexao().consultar(sql)) {
            while (rs != null && rs.next()) {
                visitantes.add(new Visitante(
                        rs.getInt("VisiCod"),
                        rs.getString("VisiNome"),
                        rs.getString("VisiCPF"),
                        rs.getString("VisiEmail"),
                        rs.getString("VisiTelefone"),
                        rs.getString("VisiEndereco"),
                        rs.getString("VisiCep"),
                        rs.getDate("VisiData_Cadastro").toLocalDate(),
                        rs.getDate("VisiData_Nascimento").toLocalDate()
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return visitantes;
    }
}
