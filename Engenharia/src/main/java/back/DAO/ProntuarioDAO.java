package back.DAO;
import back.Prontuario;

import back.Conexao.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProntuarioDAO {

    public void inserir(Prontuario prontuario) {
        String sql = "INSERT INTO prontuario (nome, rg, cpf, data_nascimento, exame_fisico, exame_complementar, crm, endereco, data_entrada, situacao) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, prontuario.getNome());
            stmt.setString(2, prontuario.getRg());
            stmt.setString(3, prontuario.getCpf());
            stmt.setString(4, prontuario.getDataNascimento());
            stmt.setString(5, prontuario.getExameFisico());
            stmt.setString(6, prontuario.getExameComplementar());
            stmt.setString(7, prontuario.getCrm());
            stmt.setString(8, prontuario.getEndereco());
            stmt.setString(9, prontuario.getDataEntrada());
            stmt.setString(10, prontuario.getSituacao());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Prontuario prontuario) {
        String sql = "UPDATE prontuario SET nome = ?, data_nascimento = ?, exame_fisico = ?, exame_complementar = ?, crm = ?, endereco = ?, data_entrada = ?, situacao = ? WHERE cpf = ?";

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, prontuario.getNome());
            stmt.setString(2, prontuario.getDataNascimento());
            stmt.setString(3, prontuario.getExameFisico());
            stmt.setString(4, prontuario.getExameComplementar());
            stmt.setString(5, prontuario.getCrm());
            stmt.setString(6, prontuario.getEndereco());
            stmt.setString(7, prontuario.getDataEntrada());
            stmt.setString(8, prontuario.getSituacao());
            stmt.setString(9, prontuario.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
