package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.dto.TopLivreStat;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    
    // Compter les emprunts en cours (non retournés)
    int countByDateRetourEffectiveIsNull();
    
    // Compter les emprunts en retard
    int countByDateRetourPrevueBeforeAndDateRetourEffectiveIsNull(LocalDate date);
    
    // Top 5 livres les plus empruntés
    @Query("SELECT new com.example.bibliotheque_quali_dev.dto.TopLivreStat(l.titre, COUNT(e)) " +
           "FROM Emprunt e JOIN e.livre l " +
           "GROUP BY l.idLivre, l.titre " +
           "ORDER BY COUNT(e) DESC")
    List<TopLivreStat> findTop5MostBorrowedBooks();
}
