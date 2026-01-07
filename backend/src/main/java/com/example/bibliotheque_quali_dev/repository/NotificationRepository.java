package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

/**
 * Repository pour l'entité Notification.
 */
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    
    /**
     * Vérifie si une notification existe pour un emprunt et un type donnés.
     */
    boolean existsByIdEmpruntAndType(Integer idEmprunt, String type);
    
    /**
     * Vérifie si une notification existe pour un emprunt, un type et une date d'envoi donnés.
     */
    boolean existsByIdEmpruntAndTypeAndDateEnvoi(Integer idEmprunt, String type, Date dateEnvoi);
    
    /**
     * Récupère toutes les notifications d'un utilisateur via les emprunts.
     */
    @Query("SELECT n FROM Notification n JOIN Emprunt e ON n.idEmprunt = e.idEmprunt WHERE e.idUser = :userId ORDER BY n.dateEnvoi DESC")
    List<Notification> findNotificationsByUserId(@Param("userId") Integer userId);
    
    /**
     * Récupère les notifications non lues d'un utilisateur.
     */
    @Query("SELECT n FROM Notification n JOIN Emprunt e ON n.idEmprunt = e.idEmprunt WHERE e.idUser = :userId AND n.lue = false ORDER BY n.dateEnvoi DESC")
    List<Notification> findUnreadByUserId(@Param("userId") Integer userId);
    
    /**
     * Compte les notifications non lues d'un utilisateur.
     */
    @Query("SELECT COUNT(n) FROM Notification n JOIN Emprunt e ON n.idEmprunt = e.idEmprunt WHERE e.idUser = :userId AND n.lue = false")
    Long countUnreadByUserId(@Param("userId") Integer userId);
}
