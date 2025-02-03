package com.example.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final long JWT_VALIDITY = 60L * 60 * 1000;
    private static final long CONFIRMATION_JWT_VALIDITY = 15L * 60 * 1000;
    private final SecretKey key;

    public JwtUtil(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    //    Génére un token à partir des informations de l'utilisateur.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        String subject = userDetails.getUsername();
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expiration = new Date(nowMillis + JWT_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();                             // Construit le token en String
    }

    //    Génére un token de confirmation pour valider l'inscription d'un nouvel utilisateur.
    public String generateConfirmationToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("type","confirmation");

        String subject = userDetails.getUsername();
        long nowMillis = System.currentTimeMillis();
        Date issuedAt = new Date(nowMillis);
        Date expiration = new Date(nowMillis + CONFIRMATION_JWT_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    //    Valide l'authenticité et la date d'expiration du token.
    public Boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        }catch (Exception e) {
            return false;
        }
    }

    public Boolean isConfirmationToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String tokenType = claims.get("type", String.class);
            return "confirmation".equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    //    Extrait le nom d'utilisateur ou d'autres claims du token.
    public void extractClaims(String token, Map<String, Object> claims) {
        try {
            Claims jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            claims.putAll(jwtClaims);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction des claims: " + e.getMessage()); // TODO: ajouter un logger plutot qu'utiliser System.err
        }
    }
}