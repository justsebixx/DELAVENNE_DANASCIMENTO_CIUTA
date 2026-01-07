package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.dto.LoginRequest;
import com.example.bibliotheque_quali_dev.dto.LoginResponse;
import com.example.bibliotheque_quali_dev.dto.RegisterRequest;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.entity.SessionToken;
import com.example.bibliotheque_quali_dev.entity.Role;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.service.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur gérant l'authentification et l'inscription des utilisateurs.
 * Utilise BCrypt pour le hachage sécurisé des mots de passe.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    @Autowired
    private TokenGenerator tokenGenerator;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Authentifie un utilisateur avec email et mot de passe.
     * @param loginRequest contient email et mot de passe
     * @return LoginResponse avec token et infos utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect."));

        // Vérification sécurisée du mot de passe avec BCrypt
        if (!passwordEncoder.matches(loginRequest.getPassword(), utilisateur.getPasswordhash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.");
        }

        // Génération d'un token de session
        String token = tokenGenerator.generateToken();
        SessionToken sessionToken = new SessionToken();
        sessionToken.setToken(token);
        sessionToken.setIdUser(utilisateur.getIdUser());
        sessionToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        sessionTokenRepository.save(sessionToken);

        // Retour de la réponse avec le token et les informations de l'utilisateur
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setIdUser(utilisateur.getIdUser());
        response.setNom(utilisateur.getNom());
        response.setPrenom(utilisateur.getPrenom());
        response.setEmail(utilisateur.getEmail());
        response.setRole(utilisateur.getRole().name());
        return ResponseEntity.ok(response);
    }

    /**
     * Enregistre un nouvel utilisateur.
     * Le mot de passe est haché avec BCrypt avant stockage.
     * @param registerRequest contient les informations du nouvel utilisateur
     * @return Message de confirmation
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        if (utilisateurRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }

        // Création du nouvel utilisateur avec mot de passe haché
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setPasswordhash(passwordEncoder.encode(registerRequest.getPassword()));
        utilisateur.setRole(Role.ETUDIANT);
        utilisateurRepository.save(utilisateur);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Utilisateur créé avec succès.");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String token) {
        // Suppression du token de session
        sessionTokenRepository.deleteByToken(token);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Déconnexion réussie.");
        return ResponseEntity.ok(response);
    }
}
