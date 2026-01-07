package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.dto.TopLivreStat;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
    
    int countByDateRetourEffectiveIsNull();
    int countByDateRetourPrevueBeforeAndDateRetourEffectiveIsNull(LocalDate date);
    @Query("SELECT new com.example.bibliotheque_quali_dev.dto.TopLivreStat(l.titre, COUNT(e)) " +
           "FROM Emprunt e JOIN e.livre l " +
           "GROUP BY l.idLivre, l.titre " +
           "ORDER BY COUNT(e) DESC")
    List<TopLivreStat> findTop5MostBorrowedBooks();
    List<Emprunt> findByDateRetourPrevueAndDateRetourEffectiveIsNull(Date dateRetourPrevue);
    @Query("SELECT e FROM Emprunt e WHERE e.dateRetourPrevue < :today AND e.dateRetourEffective IS NULL")
    List<Emprunt> findOverdueEmprunts(@Param("today") Date today);
}

