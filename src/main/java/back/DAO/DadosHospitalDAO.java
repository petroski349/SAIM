package back.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import back.Conexao.Conexao;
import back.DadosHospital;

public class DadosHospitalDAO {

    public void salvar(DadosHospital dh) {
        String sql = "INSERT INTO dados_hospital (email, telefone, endereco, horario_inicio, horario_fim, cnpj) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dh.getEmail());
            stmt.setString(2, dh.getTelefone());
            stmt.setString(3, dh.getEndereco());
            stmt.setString(4, dh.getHorarioInicio());
            stmt.setString(5, dh.getHorarioFim());
            stmt.setString(6, dh.getCnpj());

            stmt.executeUpdate();
            System.out.println("Dados do hospital salvos com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro ao salvar dados do hospital: " + e.getMessage());
        }
    }

    public DadosHospital buscar() {
        String sql = "SELECT * FROM dados_hospital ORDER BY id DESC LIMIT 1";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                DadosHospital dados = new DadosHospital();
                dados.setEmail(rs.getString("email"));
                dados.setTelefone(rs.getString("telefone"));
                dados.setEndereco(rs.getString("endereco"));
                dados.setHorarioInicio(rs.getString("horario_inicio"));
                dados.setHorarioFim(rs.getString("horario_fim"));
                dados.setCnpj(rs.getString("cnpj"));
                return dados;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
