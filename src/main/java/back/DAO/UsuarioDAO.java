package back.DAO;

import back.Usuario;
import back.Conexao.Conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {
    public Usuario buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM usuarios WHERE cpf = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id"));
                u.setNome(rs.getString("nome"));
                u.setCargo(rs.getString("cargo"));
                u.setCrm(rs.getString("crm"));
                u.setCpf(rs.getString("cpf"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
