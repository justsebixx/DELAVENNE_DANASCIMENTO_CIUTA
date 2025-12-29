package com.example.bibliotheque_quali_dev.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_tokens")
public class SessionToken {
    @Id
    private String token;

    private Integer idUser;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public SessionToken() {
    }

    public SessionToken(String token, Integer idUser, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.token = token;
        this.idUser = idUser;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
