package back.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import back.Conexao.Conexao;
import back.Medico;

public class MedicoDAO {

    // Método para cadastrar um novo médico
    public boolean cadastrarMedico(Medico medico) {
        String sql = "INSERT INTO medicos (usu_id, med_nome, med_crm, med_especialidade, med_telefone, med_email, med_horario_atendimento) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, medico.getUsuarioId());
            stmt.setString(2, medico.getNome());
            stmt.setString(3, medico.getCrm());
            stmt.setString(4, medico.getEspecialidade());
            stmt.setString(5, medico.getTelefone());
            stmt.setString(6, medico.getEmail());
            stmt.setString(7, medico.getHorarioAtendimento());

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar médico: " + e.getMessage());
            return false;
        }
    }

    // Método para buscar médico por ID
    public Medico buscarPorId(int id) {
        String sql = "SELECT * FROM medicos WHERE med_id = ? AND med_ativo = true";
        Medico medico = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setId(rs.getInt("med_id"));
                medico.setUsuarioId(rs.getInt("usu_id"));
                medico.setNome(rs.getString("med_nome"));
                medico.setCrm(rs.getString("med_crm"));
                medico.setEspecialidade(rs.getString("med_especialidade"));
                medico.setTelefone(rs.getString("med_telefone"));
                medico.setEmail(rs.getString("med_email"));
                medico.setHorarioAtendimento(rs.getString("med_horario_atendimento"));
                medico.setDataCadastro(rs.getTimestamp("med_data_cadastro"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar médico por ID: " + e.getMessage());
        }

        return medico;
    }

    // Método para buscar médico por CRM
    public Medico buscarPorCrm(String crm) {
        String sql = "SELECT * FROM medicos WHERE med_crm = ? AND med_ativo = true";
        Medico medico = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, crm);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                medico = new Medico();
                medico.setId(rs.getInt("med_id"));
                medico.setUsuarioId(rs.getInt("usu_id"));
                medico.setNome(rs.getString("med_nome"));
                medico.setCrm(rs.getString("med_crm"));
                medico.setEspecialidade(rs.getString("med_especialidade"));
                medico.setTelefone(rs.getString("med_telefone"));
                medico.setEmail(rs.getString("med_email"));
                medico.setHorarioAtendimento(rs.getString("med_horario_atendimento"));
                medico.setDataCadastro(rs.getTimestamp("med_data_cadastro"));
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar médico por CRM: " + e.getMessage());
        }

        return medico;
    }

    // Método para listar todos os médicos ativos
    public List<Medico> listarTodos() {
        String sql = "SELECT * FROM medicos WHERE med_ativo = true ORDER BY med_nome";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("med_id"));
                medico.setUsuarioId(rs.getInt("usu_id"));
                medico.setNome(rs.getString("med_nome"));
                medico.setCrm(rs.getString("med_crm"));
                medico.setEspecialidade(rs.getString("med_especialidade"));
                medico.setTelefone(rs.getString("med_telefone"));
                medico.setEmail(rs.getString("med_email"));
                medico.setHorarioAtendimento(rs.getString("med_horario_atendimento"));
                medico.setDataCadastro(rs.getTimestamp("med_data_cadastro"));

                medicos.add(medico);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar médicos: " + e.getMessage());
        }

        return medicos;
    }

    // Método para atualizar informações do médico
    public boolean atualizarMedico(Medico medico) {
        String sql = "UPDATE medicos SET med_nome = ?, med_crm = ?, med_especialidade = ?, " +
                "med_telefone = ?, med_email = ?, med_horario_atendimento = ? " +
                "WHERE med_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, medico.getNome());
            stmt.setString(2, medico.getCrm());
            stmt.setString(3, medico.getEspecialidade());
            stmt.setString(4, medico.getTelefone());
            stmt.setString(5, medico.getEmail());
            stmt.setString(6, medico.getHorarioAtendimento());
            stmt.setInt(7, medico.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar médico: " + e.getMessage());
            return false;
        }
    }

    // Método para desativar (exclusão lógica) um médico
    public boolean desativarMedico(int id) {
        String sql = "UPDATE medicos SET med_ativo = false WHERE med_id = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao desativar médico: " + e.getMessage());
            return false;
        }
    }

    // Método para verificar se um CRM já está cadastrado (para validação)
    public boolean crmExiste(String crm) {
        String sql = "SELECT COUNT(*) FROM medicos WHERE med_crm = ? AND med_ativo = true";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, crm);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Erro ao verificar CRM: " + e.getMessage());
        }

        return false;
    }

    // Método para buscar médicos por especialidade
    public List<Medico> buscarPorEspecialidade(String especialidade) {
        String sql = "SELECT * FROM medicos WHERE med_especialidade ILIKE ? AND med_ativo = true ORDER BY med_nome";
        List<Medico> medicos = new ArrayList<>();

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + especialidade + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Medico medico = new Medico();
                medico.setId(rs.getInt("med_id"));
                medico.setUsuarioId(rs.getInt("usu_id"));
                medico.setNome(rs.getString("med_nome"));
                medico.setCrm(rs.getString("med_crm"));
                medico.setEspecialidade(rs.getString("med_especialidade"));
                medico.setTelefone(rs.getString("med_telefone"));
                medico.setEmail(rs.getString("med_email"));
                medico.setHorarioAtendimento(rs.getString("med_horario_atendimento"));
                medico.setDataCadastro(rs.getTimestamp("med_data_cadastro"));

                medicos.add(medico);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar médicos por especialidade: " + e.getMessage());
        }

        return medicos;
    }
}