package com.example.bibliotheque_quali_dev.repository;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
}
