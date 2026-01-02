package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.entity.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpruntRepository extends JpaRepository<Emprunt, Integer> {
}
