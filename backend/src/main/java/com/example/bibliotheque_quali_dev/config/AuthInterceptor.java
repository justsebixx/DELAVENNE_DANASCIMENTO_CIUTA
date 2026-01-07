package com.example.bibliotheque_quali_dev.config;

import com.example.bibliotheque_quali_dev.entity.SessionToken;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.exception.ForbiddenException;
import com.example.bibliotheque_quali_dev.exception.UnauthorizedException;
import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String REQ_ATTR_PRINCIPAL = "auth.principal";

    private final SessionTokenRepository sessionTokenRepository;
    private final UtilisateurRepository utilisateurRepository;

    public AuthInterceptor(SessionTokenRepository sessionTokenRepository, UtilisateurRepository utilisateurRepository) {
        this.sessionTokenRepository = sessionTokenRepository;
        this.utilisateurRepository = utilisateurRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod hm)) {
            return true;
        }

        RequireRoles requireRoles = hm.getMethodAnnotation(RequireRoles.class);
        if (requireRoles == null) {
            requireRoles = hm.getBeanType().getAnnotation(RequireRoles.class);
        }

        // endpoints non protégés
        if (requireRoles == null) {
            return true;
        }

        AuthPrincipal principal = authenticate(request);
        request.setAttribute(REQ_ATTR_PRINCIPAL, principal);

        if (requireRoles.value().length == 0) {
            return true;
        }
        boolean ok = Arrays.stream(requireRoles.value()).anyMatch(r -> r.equalsIgnoreCase(principal.getRole()));
        if (!ok) {
            throw new ForbiddenException("Accès interdit pour le rôle: " + principal.getRole());
        }
        return true;
    }

    private AuthPrincipal authenticate(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null || auth.isBlank() || !auth.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token manquant");
        }
        String tokenValue = auth.substring("Bearer ".length()).trim();
        if (tokenValue.isBlank()) {
            throw new UnauthorizedException("Token invalide");
        }

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
        return new AuthPrincipal(user.getIdUser(), role);
    }
}
