package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.dto.LoginRequest;
import com.example.bibliotheque_quali_dev.dto.LoginResponse;
import com.example.bibliotheque_quali_dev.dto.RegisterRequest;
import com.example.bibliotheque_quali_dev.entity.Role;
import com.example.bibliotheque_quali_dev.entity.SessionToken;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.exception.UnauthorizedException;
import com.example.bibliotheque_quali_dev.exception.UtilisateurAlreadyExistsException;
import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service de gestion de l'authentification et de l'autorisation.
 * Gère le login, le register et la création de tokens de session.
 */
@Service
public class AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final SessionTokenRepository sessionTokenRepository;
    private final TokenGenerator tokenGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UtilisateurRepository utilisateurRepository,
                      SessionTokenRepository sessionTokenRepository,
                      TokenGenerator tokenGenerator) {
        this.utilisateurRepository = utilisateurRepository;
        this.sessionTokenRepository = sessionTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Authentifie un utilisateur et crée une session.
     * 
     * @param request contient email et mot de passe
     * @return LoginResponse avec token et informations utilisateur
     * @throws UnauthorizedException si les identifiants sont invalides
     */
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByEmail(request.getEmail());
        
        if (userOpt.isEmpty()) {
            throw new UnauthorizedException("Email ou mot de passe incorrect");
        }
        
        Utilisateur user = userOpt.get();
        
        // Vérifier le mot de passe
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordhash())) {
            throw new UnauthorizedException("Email ou mot de passe incorrect");
        }
        
        // Générer un token de session
        String tokenValue = tokenGenerator.generateToken();
        SessionToken token = new SessionToken();
        token.setToken(tokenValue);
        token.setIdUser(user.getIdUser());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        sessionTokenRepository.save(token);
        
        // Créer la réponse
        LoginResponse response = new LoginResponse();
        response.setToken(tokenValue);
        response.setIdUser(user.getIdUser());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        
        return response;
    }

    /**
     * Enregistre un nouvel utilisateur.
     * Par défaut, le rôle ETUDIANT est attribué.
     * 
     * @param request contient les informations du nouvel utilisateur
     * @return LoginResponse avec token et informations utilisateur
     * @throws UtilisateurAlreadyExistsException si l'email existe déjà
     */
    @Transactional
    public LoginResponse register(RegisterRequest request) {
        // Vérifier si l'email existe déjà
        if (utilisateurRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UtilisateurAlreadyExistsException("Un utilisateur avec cet email existe déjà");
        }
        
        // Créer le nouvel utilisateur
        Utilisateur user = new Utilisateur();
        user.setNom(request.getNom());
        user.setPrenom(request.getPrenom());
        user.setEmail(request.getEmail());
        user.setPasswordhash(passwordEncoder.encode(request.getPassword()));
        
        // Définir le rôle (ETUDIANT par défaut, sauf si spécifié)
        Role role = Role.ETUDIANT;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Si le rôle n'est pas valide, garder ETUDIANT par défaut
                role = Role.ETUDIANT;
            }
        }
        user.setRole(role);
        
        utilisateurRepository.save(user);
        
        // Générer un token de session
        String tokenValue = tokenGenerator.generateToken();
        SessionToken token = new SessionToken();
        token.setToken(tokenValue);
        token.setIdUser(user.getIdUser());
        token.setExpiresAt(LocalDateTime.now().plusHours(24));
        sessionTokenRepository.save(token);
        
        // Créer la réponse
        LoginResponse response = new LoginResponse();
        response.setToken(tokenValue);
        response.setIdUser(user.getIdUser());
        response.setNom(user.getNom());
        response.setPrenom(user.getPrenom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        
        return response;
    }

    /**
     * Déconnecte un utilisateur en supprimant son token de session.
     * 
     * @param token le token de session à invalider
     */
    @Transactional
    public void logout(String token) {
        sessionTokenRepository.deleteById(token);
    }
}
