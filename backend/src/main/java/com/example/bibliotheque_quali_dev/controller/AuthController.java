package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.dto.LoginRequest;
import com.example.bibliotheque_quali_dev.dto.LoginResponse;
import com.example.bibliotheque_quali_dev.dto.RegisterRequest;
import com.example.bibliotheque_quali_dev.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur gérant l'authentification et l'inscription des utilisateurs.
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect."));

        if (!utilisateur.getPasswordhash().equals(loginRequest.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.");
        }

        // Génération d'un token de session
        String token = tokenGenerator.newToken();
        SessionToken sessionToken = new SessionToken();
        sessionToken.setToken(token);
        sessionToken.setIdUser(utilisateur.getIdUser());
        sessionToken.setExpiresAt(LocalDateTime.now().plusHours(24));
        sessionTokenRepository.save(sessionToken);

        // Retour de la réponse avec le token et les informations de l'utilisateur
        LoginResponse response = new LoginResponse(token, utilisateur.getIdUser(), utilisateur.getRole());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterRequest registerRequest) {
        if (utilisateurRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
        }

        // Création du nouvel utilisateur
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(registerRequest.getNom());
        utilisateur.setPrenom(registerRequest.getPrenom());
        utilisateur.setEmail(registerRequest.getEmail());
        utilisateur.setPasswordhash(registerRequest.getPassword()); 
        utilisateur.setRole("USER");
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
