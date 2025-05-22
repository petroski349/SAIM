package com.example.saim.Controllers.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JWTTokenProvider {

    private static final SecretKey CHAVE = Keys.hmacShaKeyFor(
            "MINHACHAVESECRETA_MINHACHAVESECRETA".getBytes(StandardCharsets.UTF_8));

    static public String makeToken(Long userId, String nome) {
        String jwtToken = Jwts.builder()
                .setSubject(String.valueOf(userId)) // Define o ID como o subject
                .setIssuer("localhost:8080")
                .claim("nome", nome)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(10L)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(CHAVE) // Assina o token com a chave secreta
                .compact();
        return jwtToken;
    }


    static public boolean verifyToken(String token)
    {
        System.out.println("verifyToken");
        try {
            Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token).getSignature();
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    static public Claims getAllClaimsFromToken(String token)
    {
        Claims claims=null;
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(CHAVE)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Erro ao recuperar as informações (claims)");
        }
        return claims;
    }

    static public Long getUserId(String Authorization){
        Claims claims = getAllClaimsFromToken(Authorization);
        String userIdString = claims.getSubject(); // Recupera o "subject"
        Long userId = Long.parseLong(userIdString); // Converte para Long, se necessário
        return  userId;
    }
}

