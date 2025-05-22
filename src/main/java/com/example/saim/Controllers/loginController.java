package com.example.saim.Controllers;

import com.example.saim.Controllers.Security.JWTTokenProvider;
import com.example.saim.Models.Entitys.LoginRequest;
import com.example.saim.Models.Entitys.Usuario;
import com.example.saim.Models.Service.UsuarioService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "login/")
public class loginController {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest usuarioService) {

        //return ResponseEntity.ok(jwtTokenProvider.makeToken(user.getId(),user.getName()));
    }
    @PostMapping(value = "cadastro")
    public ResponseEntity<Object> add(@RequestBody Usuario user) {
        if(userRepository.findByEmail(user.getEmail()) == null){
            user = new User(user.getName(), user.getPassword(), user.getEmail(), LocalDateTime.now());
            System.out.println(user.getCreationDate());
            try {
                user = userRepository.save(user);
                return ResponseEntity.ok(user);
            }catch (Exception e) {
                return ResponseEntity.badRequest().body("ERRO");
            }
        }
        return ResponseEntity.badRequest().body("email ja cadastrado");
    }

}
