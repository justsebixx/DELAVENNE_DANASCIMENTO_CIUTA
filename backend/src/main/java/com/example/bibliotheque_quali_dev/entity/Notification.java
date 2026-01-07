package com.example.bibliotheque_quali_dev.entity;

import jakarta.persistence.*;

import java.sql.Date;

/**
 * Entité représentant une notification envoyée à un utilisateur.
 */
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idNotif;
    private Integer idEmprunt;
    private Date dateEnvoi;
    private String type;
    private boolean lue = false;

    public Notification(Integer idNotif, Integer idEmprunt, Date dateEnvoi) {
        this.idNotif = idNotif;
        this.idEmprunt = idEmprunt;
        this.dateEnvoi = dateEnvoi;
    }

    public Notification() {
    }

    public Integer getIdNotif() {
        return idNotif;
    }

    public void setIdNotif(Integer idNotif) {
        this.idNotif = idNotif;
    }

    public Integer getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(Integer idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isLue() {
        return lue;
    }

    public void setLue(boolean lue) {
        this.lue = lue;
    }
}

