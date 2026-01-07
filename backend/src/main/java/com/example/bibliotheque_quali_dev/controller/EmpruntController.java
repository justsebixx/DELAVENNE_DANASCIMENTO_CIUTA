package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.dto.EmpruntCreateRequest;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.service.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des emprunts.
 */
@RestController
@RequestMapping("/api/emprunts")
@CrossOrigin(origins = "*")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    /**
     * Récupère tous les emprunts.
     */
    @GetMapping
    public ResponseEntity<List<Emprunt>> getAllEmprunts() {
        return ResponseEntity.ok(empruntService.findAll());
    }

    /**
     * Récupère un emprunt par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Emprunt> getEmpruntById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(empruntService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère les emprunts actifs d'un utilisateur.
     */
    @GetMapping("/user/{userId}/actifs")
    public ResponseEntity<List<Emprunt>> getActiveEmpruntsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(empruntService.findActiveByUserId(userId));
    }

    /**
     * Récupère l'historique complet des emprunts d'un utilisateur.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Emprunt>> getEmpruntHistoryByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(empruntService.findHistoryByUserId(userId));
    }

    /**
     * Récupère tous les emprunts en retard.
     */
    @GetMapping("/retards")
    public ResponseEntity<List<Emprunt>> getOverdueEmprunts() {
        return ResponseEntity.ok(empruntService.findOverdueEmprunts());
    }

    /**
     * Crée un nouvel emprunt.
     */
    @PostMapping
    public ResponseEntity<?> createEmprunt(@RequestBody EmpruntCreateRequest request) {
        try {
            Emprunt emprunt = empruntService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Retourne un livre (marque l'emprunt comme terminé).
     */
    @PutMapping("/{id}/retour")
    public ResponseEntity<?> returnBook(@PathVariable Integer id) {
        try {
            Emprunt emprunt = empruntService.returnBook(id);
            return ResponseEntity.ok(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Prolonge la durée d'un emprunt.
     */
    @PutMapping("/{id}/prolonger")
    public ResponseEntity<?> extendEmprunt(@PathVariable Integer id) {
        try {
            Emprunt emprunt = empruntService.extend(id);
            return ResponseEntity.ok(emprunt);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
