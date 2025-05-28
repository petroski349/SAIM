package back.DAO;

import back.Paciente;
import back.Conexao.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PacienteDAO {

    public Paciente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM pacientes WHERE cpf = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Paciente buscarPorNome(String nome) {
        String sql = "SELECT * FROM pacientes WHERE nome ILIKE ?";
        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + nome + "%"); // Busca parcial

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Paciente p = new Paciente();
                p.setId(rs.getInt("id"));
                p.setNome(rs.getString("nome"));
                p.setCpf(rs.getString("cpf"));
                return p;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
