package com.example.bibliotheque_quali_dev.entity;
import jakarta.persistence.*;

@Entity
@Table(name = "livres")
public class Livre{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLivre;

    private String titre;
    private String auteur;
    private String categorie;
    private String isbn;
    private Integer annee;
    private Integer nb_exemplaires;
    private Integer nb_disponibles;

    public Livre(Integer idLivre, String titre, String auteur,
                 String categorie, String isbn, Integer annee,
                 Integer nb_exemplaires, Integer nb_disponibles)
    {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.isbn = isbn;
        this.annee = annee;
        this.nb_exemplaires = nb_exemplaires;
        this.nb_disponibles = nb_disponibles;
    }

    public Livre() {
    }

    // les getters et setters
    public Integer getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(Integer idLivre) {
        this.idLivre = idLivre;
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
