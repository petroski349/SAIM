package com.example.saim.Models.Service;

import com.example.saim.Models.Entitys.LoginRequest;
import com.example.saim.Models.Service.dals.UsuarioDal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDal usuarioDal;
    Conexao conexao = new Conexao();

    public usuario getUsuario(String login) {
        usuarioDal.
    }

    public void createUsuario() {}

    public void updateUsuario() {}
}
