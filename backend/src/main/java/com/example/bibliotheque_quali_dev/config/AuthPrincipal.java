package com.example.bibliotheque_quali_dev.config;

public class AuthPrincipal {
    private final Integer idUser;
    private final String role;

    public AuthPrincipal(Integer idUser, String role) {
        this.idUser = idUser;
        this.role = role;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getRole() {
        return role;
    }
}
