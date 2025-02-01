package com.example.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final  String SECRET_KEY = "maCleSecrete";
    private static final  long JWT_VALIDITY = 3600L * 1000;
    private static final  SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));


//    Génére un token à partir des informations de l'utilisateur.
    private String generateToken(UserDetails userDetails) {
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

    //    Valide l'authenticité et la date d'expiration du token.
    private Boolean isTokenValid(String token) {

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Date expiration = claims.getExpiration();
        return expiration.after(new Date());
    }

//    Extrait le nom d'utilisateur ou d'autres claims du token.
    private void extractClaims(String token, Map<String, Object> claims) {

        Claims jwtClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        claims.putAll(jwtClaims);
    }
}