package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des livres.
 */
@RestController
@RequestMapping("/livres")
@CrossOrigin(origins = "*")
public class LivreController {

    @Autowired
    private LivreService livreService;

    /**
     * Récupère tous les livres.
     */
    @GetMapping
    public ResponseEntity<List<Livre>> getAllLivres() {
        return ResponseEntity.ok(livreService.findAll());
    }

    /**
     * Récupère un livre par son ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(livreService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crée un nouveau livre.
     */
    @PostMapping
    public ResponseEntity<Livre> createLivre(@RequestBody Livre livre) {
        try {
            Livre created = livreService.create(livre);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Met à jour un livre existant.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Livre> updateLivre(@PathVariable Integer id, @RequestBody Livre livre) {
        try {
            Livre updated = livreService.update(id, livre);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Supprime un livre.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLivre(@PathVariable Integer id) {
        try {
            livreService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Recherche des livres selon des critères.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Livre>> searchLivres(
            @RequestParam(required = false) String titre,
            @RequestParam(required = false) String auteur,
            @RequestParam(required = false) String categorie,
            @RequestParam(required = false) Boolean disponible) {
        List<Livre> livres = livreService.search(titre, auteur, categorie, disponible);
        return ResponseEntity.ok(livres);
    }

    /**
     * Récupère toutes les catégories distinctes.
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(livreService.findAllCategories());
    }
}
