package com.example.backend.security;

import com.example.backend.services.implementations.CustomUserDetailsService;
import com.example.backend.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil; // Classe utilitaire pour gérer le JWT
    private final CustomUserDetailsService userDetailsService; // Service pour charger l'utilisateur

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws java.io.IOException, jakarta.servlet.ServletException {

        //recupération du JWT depuis les cookies
        Cookie[] cookies = request.getCookies();
        String jwtToken = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("authToken".equals(cookie.getName())) {
                    jwtToken = cookie.getValue();
                    break;
                }
            }
        }

        // Si aucun token trouvé, continuer sans authentification
        if (jwtToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Valider le JWT
        String username = jwtUtil.extractUsername(jwtToken);
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null && jwtUtil.isTokenValid(jwtToken)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Définir l'utilisateur authentifié dans le SecurityContext
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
