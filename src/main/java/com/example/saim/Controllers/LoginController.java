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
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String login = loginRequest.getEmail();
        String senha = loginRequest.getPassword();

        Usuario usuario = usuarioService.getUsuario(login);

        if (usuario == null || !usuario.getSenha().equals(senha)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Gera o token JWT
        String token = JWTTokenProvider.generateToken(usuario.getId(), usuario.getEmail());

        // Retorna o token no header Authorization e opcionalmente no corpo
        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + token)
                .body(new LoginResponse(token, usuario));
    }

    // Criação do usuário continua igual
    @PostMapping
    public ResponseEntity<String> criarUsuario(@RequestBody Usuario usuario) {
        boolean criado = usuarioService.createUsuario(usuario);
        if (criado) {
            return ResponseEntity.ok("Usuário criado com sucesso.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao criar usuário.");
    }

    // Atualizar senha continua igual
    @PutMapping("/{id}/senha")
    public ResponseEntity<String> atualizarSenha(@PathVariable int id, @RequestBody String novaSenha) {
        Usuario usuario = usuarioService.getUsuarioPorId(id);
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

    // Endpoint de exemplo para validar token e retornar usuário
    @GetMapping("/me")
    public ResponseEntity<Usuario> getUsuarioLogado(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authorizationHeader.substring(7);

        if (!JWTTokenProvider.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long usuarioId = JWTTokenProvider.getUsuarioId(token);
        if (usuarioId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioService.getUsuarioPorId(usuarioId.intValue());
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(usuario);
    }

    // Classe auxiliar para resposta do login
    private static class LoginResponse {
        private String token;
        private Usuario usuario;

        public LoginResponse(String token, Usuario usuario) {
            this.token = token;
            this.usuario = usuario;
        }

        public String getToken() {
            return token;
        }

        public Usuario getUsuario() {
            return usuario;
        }
    }
}
