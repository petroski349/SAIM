package com.example.saim.Models.Service;
import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.dals.UsuarioDal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioDal usuarioDal;

    // Busca usuário pelo login (email)
    public Usuario getUsuario(String login) {
        if (login == null || login.isEmpty()) {
            return null;
        }
        return usuarioDal.getByLogin(login);
    }

    // Cria um usuário novo
    public boolean createUsuario(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        return usuarioDal.create(usuario);
    }

    // Atualiza senha do usuário
    public boolean updateUsuarioSenha(Usuario usuario) {
        if (usuario == null) {
            return false;
        }
        return usuarioDal.updateSenha(usuario);
    }

    public Usuario getUsuarioPorId(int id) {
        if (id <= 0) {
            return null;
        }
        return usuarioDal.filterGet(id);
    }

}

