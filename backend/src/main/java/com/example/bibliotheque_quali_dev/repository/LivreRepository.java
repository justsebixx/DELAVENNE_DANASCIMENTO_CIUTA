package com.example.bibliotheque_quali_dev.repository;
import com.example.bibliotheque_quali_dev.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Integer> {
    
    @Query("SELECT COALESCE(SUM(l.nbDisponibles), 0) FROM Livre l")
    int sumNbDisponibles();
    
    @Query("SELECT COALESCE(SUM(l.nbExemplaires), 0) FROM Livre l")
    int sumNbExemplaires();
}
