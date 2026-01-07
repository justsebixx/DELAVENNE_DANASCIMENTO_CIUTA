package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.config.RequireRoles;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Contrôleur de gestion des utilisateurs.
 * Accessible uniquement aux ADMIN.
 */
@RestController
@RequestMapping("/utilisateurs")
@CrossOrigin(origins = "*")
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Récupère tous les utilisateurs.
     * Accessible uniquement aux ADMIN.
     */
    @GetMapping
    @RequireRoles({"ADMIN"})
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurRepository.findAll());
    }

    /**
     * Récupère un utilisateur par son ID.
     * Accessible uniquement aux ADMIN.
     */
    @GetMapping("/{id}")
    @RequireRoles({"ADMIN"})
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Integer id) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));
        return ResponseEntity.ok(user);
    }

    /**
     * Récupère le profil de l'utilisateur connecté.
     */
    @GetMapping("/me")
    @RequireRoles({})
    public ResponseEntity<Utilisateur> getMyProfile(jakarta.servlet.http.HttpServletRequest request) {
        com.example.bibliotheque_quali_dev.config.AuthPrincipal principal = 
            (com.example.bibliotheque_quali_dev.config.AuthPrincipal) request.getAttribute("auth.principal");
        Utilisateur user = utilisateurRepository.findById(principal.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));
        return ResponseEntity.ok(user);
    }

    /**
     * Met à jour le profil de l'utilisateur connecté.
     */
    @PutMapping("/me")
    @RequireRoles({})
    public ResponseEntity<Utilisateur> updateMyProfile(@RequestBody Utilisateur updates, jakarta.servlet.http.HttpServletRequest request) {
        com.example.bibliotheque_quali_dev.config.AuthPrincipal principal = 
            (com.example.bibliotheque_quali_dev.config.AuthPrincipal) request.getAttribute("auth.principal");
        Utilisateur user = utilisateurRepository.findById(principal.getIdUser())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));
        
        if (updates.getNom() != null && !updates.getNom().isBlank()) user.setNom(updates.getNom());
        if (updates.getPrenom() != null && !updates.getPrenom().isBlank()) user.setPrenom(updates.getPrenom());
        if (updates.getEmail() != null && !updates.getEmail().isBlank()) {
             // Vérifier si l'email existe déjà si changé
             if (!user.getEmail().equals(updates.getEmail()) && utilisateurRepository.findByEmail(updates.getEmail()).isPresent()) {
                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cet email est déjà utilisé.");
             }
             user.setEmail(updates.getEmail());
        }
        
        return ResponseEntity.ok(utilisateurRepository.save(user));
    }

    /**
     * Supprime un utilisateur.
     * Accessible uniquement aux ADMIN.
     */
    @DeleteMapping("/{id}")
    @RequireRoles({"ADMIN"})
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé.");
        }
        utilisateurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
