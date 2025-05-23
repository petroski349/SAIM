package com.example.saim.Models.Service.dals;

import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.SingletonDB;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UsuarioDal implements IDAL<Usuario> {


    @Override
    public boolean create(Usuario usuario) {
        // Limpeza dos dados para garantir que não haja caracteres nulos ou inválidos
        String senha = limpaEntrada(usuario.getSenha());
        String email = limpaEntrada(usuario.getEmail());
        String nome = limpaEntrada(usuario.getNome());

        String sql = """
    INSERT INTO usuario (senha, email, tipo, nome)
    VALUES (?, ?, ?, ?);
    """;

        try (Connection con = SingletonDB.getConexao().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // Define os parâmetros do PreparedStatement
            ps.setString(1, senha);  // Senha
            ps.setString(2, email);  // Email
            ps.setString(3, String.valueOf(usuario.getTipo()));  // Tipo (char)
            ps.setString(4, nome);   // Nome

            // Executa a atualização no banco
            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;  // Retorna true se a inserção foi bem-sucedida

        } catch (SQLException e) {
            e.printStackTrace();  // Imprime o erro
            return false;  // Retorna false se houver erro
        }
    }

    private String limpaEntrada(String entrada) {
        if (entrada == null) return "";

        // Remove caracteres nulos e caracteres não ASCII e de controle
        entrada = entrada.replaceAll("[^\\x20-\\x7E\\x0A\\x0D]", "");  // Limpa caracteres não ASCII e de controle
        entrada = entrada.replace("\0", "");  // Remove caracteres nulos explicitamente

        return entrada;
    }







    @Override
    public boolean updateSenha(Usuario usuario) {
        String sql = """
            UPDATE usuario SET
            senha = '#1'
            WHERE id = #2;
            """;

        sql = sql.replace("#1", usuario.getSenha());
        sql = sql.replace("#2", String.valueOf(usuario.getId()));

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean deleate(Usuario usuario) {
        String sql = """
            DELETE FROM usuario
            WHERE id = #1;
            """;

        sql = sql.replace("#1", String.valueOf(usuario.getId()));

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public Usuario getByLogin(String login) {
        String sql = """
        SELECT * FROM usuario
        WHERE email = '#1';
    """;

        sql = sql.replace("#1", login);

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Usuario usuario = null;

        try {
            if (rs != null && rs.next()) {
                usuario = new Usuario();
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));

                // Modificado para tratar 'tipo' como String
                String tipoStr = rs.getString("tipo");
                if (tipoStr != null && !tipoStr.isEmpty()) {
                    usuario.setTipo(tipoStr);  // Agora o tipo é uma String, não um char
                }

                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }


    @Override
    public Usuario filterGet(int id) {
        // SQL para selecionar o usuário com base no id
        String sql = "SELECT * FROM usuario WHERE id = ?;";

        // Executa a consulta no banco de dados
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Usuario usuario = null;

        try {
            if (rs != null && rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));

                String tipoStr = rs.getString("tipo");
                if (tipoStr != null && !tipoStr.isEmpty()) {
                    usuario.setTipo(tipoStr);  // Agora tipo é uma String, não um char
                }
                usuario.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();  // Registra o erro caso algo aconteça durante a consulta
        }

        return usuario;
    }


}
