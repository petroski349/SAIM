package back.DAO;

import back.Conexao.Conexao;
import back.Prontuario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ProntuarioDAO {

    public void inserir(Prontuario prontuario) {
        String sql = "INSERT INTO prontuario (cpf_paciente, data, descricao) VALUES (?, ?, ?)";
        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);

            // 🔥 Remove pontos, traços e espaços do CPF ANTES de gravar
            String cpfLimpo = prontuario.getCpfPaciente().replaceAll("[^\\d]", "");

            stmt.setString(1, cpfLimpo);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now())); // ✅ Correto para timestamp
            stmt.setString(3, prontuario.getDescricao());

            stmt.executeUpdate();
            System.out.println("✅ Prontuário salvo com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao salvar prontuário: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Prontuario> listarPorCpf(String cpf) {
        List<Prontuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM prontuario WHERE cpf_paciente = ? ORDER BY data DESC";

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Prontuario p = new Prontuario();
                p.setId(rs.getInt("id"));
                p.setCpfPaciente(rs.getString("cpf_paciente"));
                p.setDescricao(rs.getString("descricao"));
                p.setDataHora(rs.getTimestamp("data").toString());

                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
