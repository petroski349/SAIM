package back.DAO;

import back.Consulta;
import back.Conexao.Conexao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public List<Consulta> buscarUltimaConsultaPorCpf(String cpf) {
        List<Consulta> lista = new ArrayList<>();
        String sql = "SELECT * FROM consulta " +
                "WHERE cpf_paciente = ? " +
                "AND data = ( " +
                "SELECT MAX(data) FROM consulta WHERE cpf_paciente = ? " +
                ") " +
                "ORDER BY id ASC";

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            String cpfLimpo = cpf.replaceAll("[^\\d]", "");
            stmt.setString(1, cpfLimpo);
            stmt.setString(2, cpfLimpo);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta c = new Consulta();
                c.setId(rs.getInt("id"));
                c.setCpfPaciente(rs.getString("cpf_paciente"));
                c.setMedicamento(rs.getString("medicamento"));
                c.setQuantidade(rs.getString("quantidade"));
                c.setPeriodo(rs.getString("periodo_aplicacao"));
                c.setData(rs.getTimestamp("data").toString());

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
