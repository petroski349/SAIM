package com.example.saim.Models.Service.dals;

import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.Conexao;
import com.example.saim.Models.Service.SingletonDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UsuarioDal implements IDAL<Usuario>{
    @Autowired
    private SingletonDB singletonDB;

    @Override
    public boolean create(Usuario usuario) {
        String sql =
                """
                INSERT INTO unidade (senha, email, data_registro, tipo)
                VALUES ('#3', '#2', '#4', '#5');
                """;
        sql = sql.replace("#1", usuario.getNome());
        sql = sql.replace("#2", usuario.getEmail());
        sql = sql.replace("#3", usuario.getSenha());
        sql = sql.replace("#4", usuario.getDataRegistro());
        sql = sql.replace("#5", usuario.getTipo());

        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean updateSenha(Usuario usuario) {
        String sql =
                """  
                UPDATE usuario SET
                senha = '#1',
                WHERE id = #2;
                """;
        sql = sql.replace("#1", usuario.getSenha());
        sql = sql.replace("#2", usuario.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public boolean deleate(Usuario usuario) {
        String sql =
                """  
                DELEATE FROM usuario
                WHERE id = #1;
                """;
        sql = sql.replace("#1", usuario.getId());
        return SingletonDB.getConexao().manipular(sql);
    }

    @Override
    public Usuario getByLogin(String  login) throws SQLException {
        String sql =
                """  
                        Select * FROM usuario
                        WHERE id = #1;
                        """;
        sql = sql.replace("#1", login);
        ResultSet rs = SingletonDB.getConexao().consultar(sql);
        Usuario usuario = new Usuario();
        while (rs != null && rs.next()) {
            usuario.setSenha(rs.getString("senha"));      // pega a senha como String
            usuario.setEmail(rs.getString("email"));     // pega o email
            usuario.setDataRegistro(rs.getDate("data_registro")); // pega a data
            usuario.setTipo(rs.getString("tipo"));       // pega o tipo (char(1) como String)
        }
        return usuario;
    }
    @Override
    public Usuario filterGet(int id) {
        return null;
    }

    @Override
    public List<Usuario> filterGet(String filtro) {
        return List.of();
    }
}



