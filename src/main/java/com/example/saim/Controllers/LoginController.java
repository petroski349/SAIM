package com.example.saim.Controllers;

import com.example.saim.Controllers.Security.JWTTokenProvider;
import com.example.saim.Models.Entitys.LoginRequest;
import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.UsuarioService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        String login = loginRequest.getEmail();
        String senha = loginRequest.getPassword();

        Usuario usuario = usuarioService.getUsuario(login);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Aqui você pode validar a senha também
        if (!usuario.getSenha().equals(senha)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(usuario);
    }


    // Exemplo para criar usuário (POST)
    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        boolean criado = usuarioService.createUsuario(usuario);
        if (criado) {
            return ResponseEntity.ok("Usuário criado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar usuário.");
    }

    // Exemplo para atualizar senha (PUT)
    @PutMapping("/{id}/senha")
    public ResponseEntity<String> atualizarSenha(@PathVariable int id, @RequestBody String novaSenha) {
        Usuario usuario = usuarioService.getUsuarioPorId(id); // você pode criar esse método na service/dal
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuario.setSenha(novaSenha);
        boolean atualizado = usuarioService.updateUsuarioSenha(usuario);
        if (atualizado) {
            return ResponseEntity.ok("Senha atualizada com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao atualizar senha.");
    }

}

