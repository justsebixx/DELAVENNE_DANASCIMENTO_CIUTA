package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
