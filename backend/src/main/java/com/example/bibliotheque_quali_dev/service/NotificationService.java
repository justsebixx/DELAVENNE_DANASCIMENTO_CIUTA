package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.entity.Notification;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import com.example.bibliotheque_quali_dev.repository.NotificationRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service de gestion et d'envoi automatique des notifications.
 * Envoie des rappels à J-30 et J-5 avant la date de retour prévue.
 * 
 * @author Bibliothèque
 * @version 1.0
 */
@Service
@Transactional
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * Tâche planifiée : vérifie et envoie les rappels à J-30.
     * S'exécute tous les jours à 8h00.
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendRemindersJ30() {
        LocalDate targetDate = LocalDate.now().plusDays(30);
        List<Emprunt> emprunts = empruntRepository.findByDateRetourPrevueAndDateRetourEffectiveIsNull(
            Date.valueOf(targetDate)
        );

        for (Emprunt emprunt : emprunts) {
            // Vérifier si une notification J-30 n'a pas déjà été envoyée
            if (!notificationRepository.existsByIdEmpruntAndType(emprunt.getIdEmprunt(), "RAPPEL_J30")) {
                createNotification(emprunt, "RAPPEL_J30");
            }
        }
    }

    /**
     * Tâche planifiée : vérifie et envoie les rappels à J-5.
     * S'exécute tous les jours à 8h00.
     */
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendRemindersJ5() {
        LocalDate targetDate = LocalDate.now().plusDays(5);
        List<Emprunt> emprunts = empruntRepository.findByDateRetourPrevueAndDateRetourEffectiveIsNull(
            Date.valueOf(targetDate)
        );

        for (Emprunt emprunt : emprunts) {
            // Vérifier si une notification J-5 n'a pas déjà été envoyée
            if (!notificationRepository.existsByIdEmpruntAndType(emprunt.getIdEmprunt(), "RAPPEL_J5")) {
                createNotification(emprunt, "RAPPEL_J5");
            }
        }
    }

    /**
     * Tâche planifiée : vérifie et notifie les emprunts en retard.
     * S'exécute tous les jours à 9h00.
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void sendOverdueNotifications() {
        List<Emprunt> overdueEmprunts = empruntRepository.findOverdueEmprunts(Date.valueOf(LocalDate.now()));

        for (Emprunt emprunt : overdueEmprunts) {
            // Envoyer une notification de retard si pas déjà envoyée aujourd'hui
            LocalDate today = LocalDate.now();
            if (!notificationRepository.existsByIdEmpruntAndTypeAndDateEnvoi(
                    emprunt.getIdEmprunt(), "RETARD", Date.valueOf(today))) {
                createNotification(emprunt, "RETARD");
            }
        }
    }

    /**
     * Crée une notification pour un emprunt.
     * @param emprunt L'emprunt concerné
     * @param type Type de notification (RAPPEL_J30, RAPPEL_J5, RETARD, RETOUR)
     */
    public void createNotification(Emprunt emprunt, String type) {
        Notification notification = new Notification();
        notification.setIdEmprunt(emprunt.getIdEmprunt());
        notification.setType(type);
        notification.setDateEnvoi(Date.valueOf(LocalDate.now()));
        
        notificationRepository.save(notification);
        
        // Log pour suivi (en production, envoyer un vrai email/SMS)
        System.out.println(String.format(
            "[NOTIFICATION] Type: %s | Emprunt ID: %d | User ID: %d | Date: %s",
            type, emprunt.getIdEmprunt(), emprunt.getIdUser(), LocalDate.now()
        ));
    }

    /**
     * Récupère les notifications d'un utilisateur.
     * @param userId Identifiant de l'utilisateur
     * @return Liste des notifications
     */
    public List<Notification> getNotificationsByUser(Integer userId) {
        return notificationRepository.findNotificationsByUserId(userId);
    }

    /**
     * Récupère les notifications non lues d'un utilisateur.
     * @param userId Identifiant de l'utilisateur
     * @return Liste des notifications non lues
     */
    public List<Notification> getUnreadNotifications(Integer userId) {
        return notificationRepository.findUnreadByUserId(userId);
    }

    /**
     * Marque une notification comme lue.
     * @param notificationId Identifiant de la notification
     * @return La notification mise à jour, ou null si non trouvée
     */
    public Notification markAsRead(Integer notificationId) {
        return notificationRepository.findById(notificationId).map(notification -> {
            notification.setLue(true);
            return notificationRepository.save(notification);
        }).orElse(null);
    }

    /**
     * Crée une notification de retour (quand un livre est retourné).
     * @param emprunt L'emprunt retourné
     */
    public void createReturnNotification(Emprunt emprunt) {
        createNotification(emprunt, "RETOUR");
    }

    /**
     * Compte le nombre de notifications non lues pour un utilisateur.
     * @param userId Identifiant de l'utilisateur
     * @return Nombre de notifications non lues
     */
    public long countUnread(Integer userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }
}
