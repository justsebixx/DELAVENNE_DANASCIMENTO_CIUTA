package com.example.bibliotheque_quali_dev.config;

import com.example.bibliotheque_quali_dev.entity.SessionToken;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.exception.UnauthorizedException;
import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private final SessionTokenRepository sessionTokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    public TokenAuthenticationFilter(SessionTokenRepository sessionTokenRepository,
                                     UtilisateurRepository utilisateurRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Si déjà authentifié, ne rien faire
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank() || !auth.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String tokenValue = auth.substring("Bearer ".length()).trim();
        if (tokenValue.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Optional<SessionToken> tokOpt = sessionTokenRepository.findById(tokenValue);
            if (tokOpt.isEmpty()) {
                throw new UnauthorizedException("Token invalide");
            }

            SessionToken token = tokOpt.get();
            if (token.getExpiresAt() != null && token.getExpiresAt().isBefore(LocalDateTime.now())) {
                sessionTokenRepository.deleteById(tokenValue);
                throw new UnauthorizedException("Token expiré");
            }

            Utilisateur user = utilisateurRepository.findById(token.getIdUser())
                    .orElseThrow(() -> new UnauthorizedException("Utilisateur introuvable pour ce token"));

            String role = user.getRole() != null ? user.getRole().name() : "ETUDIANT";
            AuthPrincipal principal = new AuthPrincipal(user.getIdUser(), role);

            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, tokenValue, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (UnauthorizedException ex) {
            // Laisser Spring Security gérer si un endpoint exige l'auth.
            // Ici on nettoie juste le contexte.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
