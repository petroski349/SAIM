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

    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "MINHACHAVESECRETA_MINHACHAVESECRETA".getBytes(StandardCharsets.UTF_8));

    /**
     * Gera o token JWT para o usuário, usando seu ID e email (ou nome)
     */
    public static String generateToken(Long usuarioId, String email) {
        return Jwts.builder()
                .setSubject(String.valueOf(usuarioId))  // Coloca o ID do usuário como subject
                .setIssuer("localhost:8080")
                .claim("email", email)                   // Guarda o email como claim
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(60L)  // Token válido por 60 min
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Verifica se o token é válido (assinatura correta e não expirado)
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
        }
        return false;
    }

    /**
     * Obtém os claims do token JWT
     */
    public static Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("Erro ao obter claims do token: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtém o ID do usuário a partir do token JWT (subject)
     */
    public static Long getUsuarioId(String token) {
        Claims claims = getClaims(token);
        if (claims == null) return null;

        String userIdStr = claims.getSubject();
        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            System.out.println("ID de usuário inválido no token");
            return null;
        }
    }

    /**
     * Opcional: obter o email do usuário pelo claim
     */
    public static String getEmail(String token) {
        Claims claims = getClaims(token);
        if (claims == null) return null;
        return claims.get("email", String.class);
    }
}
