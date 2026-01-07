package com.example.bibliotheque_quali_dev.config;

import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SessionTokenRepository sessionTokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Value("${app.cors.allowed-origins:http://localhost:5173,http://localhost:5174,http://127.0.0.1:5173,http://127.0.0.1:5174}")
    private String corsAllowedOrigins;

    public SecurityConfig(SessionTokenRepository sessionTokenRepository, UtilisateurRepository utilisateurRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new TokenAuthenticationFilter(sessionTokenRepository, utilisateurRepository),
                    UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()

                // RBAC: endpoints staff (en plus des @RequireRoles)
                .requestMatchers("/dashboard/**").hasAnyRole("ADMIN", "BIBLIOTHECAIRE")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/livres/**").hasAnyRole("ADMIN", "BIBLIOTHECAIRE")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/livres/**").hasAnyRole("ADMIN", "BIBLIOTHECAIRE")
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/livres/**").hasAnyRole("ADMIN", "BIBLIOTHECAIRE")
                .requestMatchers("/utilisateurs").hasRole("ADMIN")
                .requestMatchers("/utilisateurs/**").authenticated()

                // défaut: nécessite d'être connecté
                .anyRequest().authenticated()
            );
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        List<String> originPatterns = Arrays.stream(corsAllowedOrigins.split(","))
            .map(String::trim)
            .filter(s -> !s.isBlank())
            .toList();
        config.setAllowedOriginPatterns(originPatterns);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
