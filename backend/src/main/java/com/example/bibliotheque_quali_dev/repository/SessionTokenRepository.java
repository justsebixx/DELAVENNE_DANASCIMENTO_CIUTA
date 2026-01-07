package com.example.bibliotheque_quali_dev.repository;

import com.example.bibliotheque_quali_dev.entity.SessionToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface SessionTokenRepository extends JpaRepository<SessionToken, String> {
    long deleteByExpiresAtBefore(LocalDateTime cutoff);
    void deleteByToken(String token);
}
