package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Integer> {
    
    @Query("SELECT COALESCE(SUM(l.nbDisponibles), 0) FROM Livre l")
    int sumNbDisponibles();
    
    @Query("SELECT COALESCE(SUM(l.nbExemplaires), 0) FROM Livre l")
    int sumNbExemplaires();

    /**
     * Recherche des livres selon des critères multiples.
     */
    @Query("SELECT l FROM Livre l WHERE " +
           "(:titre IS NULL OR LOWER(l.titre) LIKE LOWER(CONCAT('%', :titre, '%'))) AND " +
           "(:auteur IS NULL OR LOWER(l.auteur) LIKE LOWER(CONCAT('%', :auteur, '%'))) AND " +
           "(:categorie IS NULL OR LOWER(l.categorie) LIKE LOWER(CONCAT('%', :categorie, '%'))) AND " +
           "(:disponible IS NULL OR (:disponible = true AND l.nbDisponibles > 0) OR (:disponible = false AND l.nbDisponibles = 0))")
    List<Livre> searchBooks(
            @Param("titre") String titre,
            @Param("auteur") String auteur,
            @Param("categorie") String categorie,
            @Param("disponible") Boolean disponible
    );

    /**
     * Récupère toutes les catégories distinctes.
     */
    @Query("SELECT DISTINCT l.categorie FROM Livre l ORDER BY l.categorie")
    List<String> findDistinctCategories();
}
