package back.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import back.AparenciaSistema;
import back.Conexao.Conexao;

public class AparenciaSistemaDAO {
    public void salvar(AparenciaSistema ap) {
        String sql = "INSERT INTO aparencia_sistema (nome, logo_base64, cor, rodape) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ap.getNome());
            stmt.setString(2, ap.getLogoBase64());
            stmt.setString(3, ap.getCor());
            stmt.setString(4, ap.getRodape());

            stmt.executeUpdate();
            System.out.println("Aparência salva com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao salvar aparência: " + e.getMessage());
        }
    }


    public AparenciaSistema buscarUltimaConfiguracao() {
        String sql = "SELECT * FROM aparencia_sistema ORDER BY id DESC LIMIT 1";

        try (Connection conn = Conexao.conectar()) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                AparenciaSistema ap = new AparenciaSistema();
                ap.setNome(rs.getString("nome"));
                ap.setLogoBase64(rs.getString("logo_base64"));
                ap.setCor(rs.getString("cor"));
                ap.setRodape(rs.getString("rodape"));
                return ap;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
