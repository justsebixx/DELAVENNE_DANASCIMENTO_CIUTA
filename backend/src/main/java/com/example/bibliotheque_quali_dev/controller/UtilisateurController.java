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
@RequireRoles({"ADMIN"})
public class UtilisateurController {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Récupère tous les utilisateurs.
     * Accessible uniquement aux ADMIN.
     */
    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUtilisateurs() {
        return ResponseEntity.ok(utilisateurRepository.findAll());
    }

    /**
     * Récupère un utilisateur par son ID.
     * Accessible uniquement aux ADMIN.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateurById(@PathVariable Integer id) {
        Utilisateur user = utilisateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé."));
        return ResponseEntity.ok(user);
    }

    /**
     * Supprime un utilisateur.
     * Accessible uniquement aux ADMIN.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {
        if (!utilisateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé.");
        }
        utilisateurRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
