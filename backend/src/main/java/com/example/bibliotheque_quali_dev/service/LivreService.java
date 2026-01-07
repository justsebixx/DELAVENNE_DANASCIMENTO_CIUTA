package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service de gestion des livres avec logique métier.
 */
@Service
@Transactional
public class LivreService {

    @Autowired
    private LivreRepository livreRepository;

    /**
     * Récupère tous les livres.
     */
    public List<Livre> findAll() {
        return livreRepository.findAll();
    }

    /**
     * Récupère un livre par son ID.
     */
    public Livre findById(Integer id) {
        return livreRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livre non trouvé avec l'ID: " + id));
    }

    /**
     * Crée un nouveau livre.
     */
    public Livre create(Livre livre) {
        // Validation
        if (livre.getNbDisponibles() > livre.getNbExemplaires()) {
            throw new RuntimeException("Le nombre d'exemplaires disponibles ne peut pas dépasser le nombre total");
        }
        return livreRepository.save(livre);
    }

    /**
     * Met à jour un livre existant.
     */
    public Livre update(Integer id, Livre livreDetails) {
        Livre livre = findById(id);
        
        livre.setTitre(livreDetails.getTitre());
        livre.setAuteur(livreDetails.getAuteur());
        livre.setCategorie(livreDetails.getCategorie());
        livre.setIsbn(livreDetails.getIsbn());
        livre.setAnnee(livreDetails.getAnnee());
        livre.setNbExemplaires(livreDetails.getNbExemplaires());
        livre.setNbDisponibles(livreDetails.getNbDisponibles());

        if (livre.getNbDisponibles() > livre.getNbExemplaires()) {
            throw new RuntimeException("Le nombre d'exemplaires disponibles ne peut pas dépasser le nombre total");
        }

        return livreRepository.save(livre);
    }

    /**
     * Supprime un livre.
     */
    public void delete(Integer id) {
        if (!livreRepository.existsById(id)) {
            throw new RuntimeException("Livre non trouvé avec l'ID: " + id);
        }
        livreRepository.deleteById(id);
    }

    /**
     * Recherche des livres selon des critères.
     */
    public List<Livre> search(String titre, String auteur, String categorie, Boolean disponible) {
        return livreRepository.searchBooks(titre, auteur, categorie, disponible);
    }

    /**
     * Récupère toutes les catégories distinctes.
     */
    public List<String> findAllCategories() {
        return livreRepository.findDistinctCategories();
    }

    /**
     * Vérifie si un livre est disponible.
     */
    public boolean isAvailable(Integer idLivre) {
        Livre livre = findById(idLivre);
        return livre.getNbDisponibles() > 0;
    }

    /**
     * Décrémente le nombre d'exemplaires disponibles.
     */
    public void decrementDisponibles(Integer idLivre) {
        Livre livre = findById(idLivre);
        if (livre.getNbDisponibles() <= 0) {
            throw new RuntimeException("Aucun exemplaire disponible pour ce livre");
        }
        livre.setNbDisponibles(livre.getNbDisponibles() - 1);
        livreRepository.save(livre);
    }

    /**
     * Incrémente le nombre d'exemplaires disponibles.
     */
    public void incrementDisponibles(Integer idLivre) {
        Livre livre = findById(idLivre);
        if (livre.getNbDisponibles() >= livre.getNbExemplaires()) {
            throw new RuntimeException("Le nombre d'exemplaires disponibles ne peut pas dépasser le total");
        }
        livre.setNbDisponibles(livre.getNbDisponibles() + 1);
        livreRepository.save(livre);
    }
}
