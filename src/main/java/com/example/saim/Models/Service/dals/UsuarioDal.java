package com.example.saim.Models.Service.dals;

import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.SingletonDB;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UsuarioDal implements IDAL<Usuario> {

    @Override
    public boolean create(Usuario usuario) {
        // Formata dataRegistro para string ISO, se null usa CURRENT_DATE
        String dataFormatada = usuario.getDataRegistro() != null
                ? usuario.getDataRegistro().toString()  // ex: "2025-05-22T15:30:00"
                : "CURRENT_DATE";

        String sql = """
            INSERT INTO usuario (senha, email, data_registro, tipo)
            VALUES ('#1', '#2', '#3', '#4');
            """;

        sql = sql.replace("#1", usuario.getSenha());
        sql = sql.replace("#2", usuario.getEmail());

        // Se for CURRENT_DATE, não deve estar entre aspas no SQL
        if ("CURRENT_DATE".equals(dataFormatada)) {
            sql = sql.replace("'#3'", dataFormatada);
        } else {
            sql = sql.replace("#3", dataFormatada);
        }

        sql = sql.replace("#4", String.valueOf(usuario.getTipo()));

        return SingletonDB.getConexao().manipular(sql);
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
    public Usuario filterGet(int id) {
        String sql = "SELECT * FROM usuario WHERE id = " + id + ";";

        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Usuario usuario = null;

        try {
            if (rs != null && rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setEmail(rs.getString("email"));

                // Usa getTimestamp para pegar data e hora corretamente
                if (rs.getTimestamp("data_registro") != null) {
                    usuario.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime());
                }

                String tipoStr = rs.getString("tipo");
                if (tipoStr != null && !tipoStr.isEmpty()) {
                    usuario.setTipo(tipoStr.charAt(0));
                }

                usuario.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }

    @Override
    public List<Usuario> filterGet(String filtro) {
        return List.of();
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

                if (rs.getTimestamp("data_registro") != null) {
                    usuario.setDataRegistro(rs.getTimestamp("data_registro").toLocalDateTime());
                }

                String tipoStr = rs.getString("tipo");
                if (tipoStr != null && !tipoStr.isEmpty()) {
                    usuario.setTipo(tipoStr.charAt(0));
                }

                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuario;
    }
}
