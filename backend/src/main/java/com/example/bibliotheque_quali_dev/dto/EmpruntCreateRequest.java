package com.example.bibliotheque_quali_dev.dto;

public class EmpruntCreateRequest {
    private Integer idLivre;
    private Integer idUser;

    public EmpruntCreateRequest() {
    }

    public Integer getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(Integer idLivre) {
        this.idLivre = idLivre;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
}
