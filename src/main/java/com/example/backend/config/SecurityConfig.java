package com.example.backend.config;

import com.example.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Value("${client.url:http://localhost:4200}")
    private String allowedOrigin;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        // Configuration de la sécurisation des endpoints
        http
                // Configuration du CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // Désactiver la protection CSRF
                .csrf(csrf -> csrf.disable())

                // Ne pas gérer les sessions côté serveur  car JWT est utilisé comme Stateless token
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configurer l'autorisation des requêtes
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/status").permitAll()
                        .requestMatchers("/auth/**").permitAll() // Autoriser librement les endpoints publics
                        .anyRequest().authenticated()           // Toutes les autres routes nécessitent une authentification
                )

                // Ajouts des filtres personnalisés
                .addFilterBefore(jwtAuthenticationFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    /**
     * Définir la configuration CORS globale.
     * @return la CORS Configuration Source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(allowedOrigin)); // Autoriser le frontend Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Méthodes HTTP autorisées
        configuration.setAllowedHeaders(List.of("*")); // Autoriser tous les en-têtes des requêtes
        configuration.setExposedHeaders(List.of("Authorization")); // Exposer certains headers si nécessaire (par exemple Authorization)
        configuration.setAllowCredentials(true); // Si nécessaire (par exemple, pour les cookies)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Appliquer CORS globalement à tous les endpoints
        return source;
    }

    /**
     * Expose l'AuthenticationManager en tant que Bean. Cela peut être utile, par exemple, pour l'authentification lors de l'utilisation de services manuels.
     * @param authenticationConfiguration la configuration de l'authentification
     * @return AuthenticationManager
     * @throws Exception en cas de problème de configuration
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
