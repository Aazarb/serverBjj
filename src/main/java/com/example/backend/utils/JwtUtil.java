package com.example.backend.utils;

import com.example.backend.exceptions.ConfirmationTokenNotFoundException;
import com.example.backend.exceptions.MissingTokenSubjectException;
import org.slf4j.Logger;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
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
    public boolean isTokenValid(String token) {
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

    //    Extrait le nom d'utilisateur ou d'autres claims du token.
    public Map<String, Object> extractClaims(String token) {
        try {
            Map<String, Object> claims = new HashMap<>();
            Claims jwtClaims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            claims.putAll(jwtClaims);
            return claims;
        } catch (Exception e) {
            logger.error(String.format("Erreur lors de l'extraction des claims: %s", e.getMessage()));
        }
        return Collections.emptyMap();
    }

    public String extractUsername(String token)  throws MissingTokenSubjectException, ConfirmationTokenNotFoundException {
        if (token == null || token.isEmpty()) {
            throw new ConfirmationTokenNotFoundException("No token found");
        }
        Map<String, Object> claims = extractClaims(token);

        String username = (String) claims.get("sub");
        if (username == null || username.isEmpty()) {
            throw new MissingTokenSubjectException("Token does not contain a subject");
        }else{
            return username;
        }
    }
}