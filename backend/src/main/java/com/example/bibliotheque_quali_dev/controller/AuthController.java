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

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint de connexion.
     * 
     * @param request contient email et mot de passe
     * @return LoginResponse avec token et informations utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint d'inscription.
     * 
     * @param request contient les informations du nouvel utilisateur
     * @return LoginResponse avec token et informations utilisateur
     */
    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        LoginResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint de déconnexion.
     * 
     * @param authorization header Authorization contenant le token Bearer
     * @return ResponseEntity sans contenu
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring("Bearer ".length()).trim();
            authService.logout(token);
        }
        return ResponseEntity.ok().build();
    }
}
