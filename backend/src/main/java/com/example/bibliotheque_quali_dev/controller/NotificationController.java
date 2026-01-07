package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Notification;
import com.example.bibliotheque_quali_dev.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des notifications.
 */
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Récupère toutes les notifications d'un utilisateur.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getNotificationsByUser(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Récupère les notifications non lues d'un utilisateur.
     */
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    /**
     * Compte les notifications non lues d'un utilisateur.
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Map<String, Long>> countUnreadNotifications(@PathVariable Integer userId) {
        Long count = notificationService.countUnread(userId);
        return ResponseEntity.ok(Map.of("count", count));
    }

    /**
     * Marque une notification comme lue.
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable Integer id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification != null) {
            return ResponseEntity.ok(notification);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Marque toutes les notifications d'un utilisateur comme lues.
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable Integer userId) {
        List<Notification> notifications = notificationService.getUnreadNotifications(userId);
        for (Notification notification : notifications) {
            notificationService.markAsRead(notification.getIdNotif());
        }
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
}
