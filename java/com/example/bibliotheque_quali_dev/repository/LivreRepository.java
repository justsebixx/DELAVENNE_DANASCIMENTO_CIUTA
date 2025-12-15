package com.example.bibliotheque_quali_dev.repository;
import com.example.bibliotheque_quali_dev.entity.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Integer> {
}
