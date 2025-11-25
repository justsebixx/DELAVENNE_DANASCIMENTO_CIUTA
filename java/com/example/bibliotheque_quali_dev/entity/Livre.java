package com.example.bibliotheque_quali_dev.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

@Entity
public class Livre{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_livre;

    private String titre;
    private String auteur;
    private String categorie;
    private String isbn;
    private Integer annee;
    private Integer nb_exemplaires;
    private Integer nb_disponibles;

    public Integer getId_livre() {
        return id_livre;
    }

    public void setId_livre(Integer id_livre) {
        this.id_livre = id_livre;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getAnnee() {
        return annee;
    }

    public void setAnnee(Integer annee) {
        this.annee = annee;
    }

    public Integer getNb_exemplaires() {
        return nb_exemplaires;
    }

    public void setNb_exemplaires(Integer nb_exemplaires) {
        this.nb_exemplaires = nb_exemplaires;
    }

    public Integer getNb_disponibles() {
        return nb_disponibles;
    }

    public void setNb_disponibles(Integer nb_disponibles) {
        this.nb_disponibles = nb_disponibles;
    }
}
