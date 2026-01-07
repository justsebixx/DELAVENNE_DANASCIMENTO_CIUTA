package com.example.bibliotheque_quali_dev.entity;
import jakarta.persistence.*;

/**
 * Entité représentant un livre dans la bibliothèque.
 */
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
    
    @Column(name = "nb_exemplaires")
    private Integer nbExemplaires;
    
    @Column(name = "nb_disponibles")
    private Integer nbDisponibles;

    public Livre(Integer idLivre, String titre, String auteur,
                 String categorie, String isbn, Integer annee,
                 Integer nbExemplaires, Integer nbDisponibles)
    {
        this.idLivre = idLivre;
        this.titre = titre;
        this.auteur = auteur;
        this.categorie = categorie;
        this.isbn = isbn;
        this.annee = annee;
        this.nbExemplaires = nbExemplaires;
        this.nbDisponibles = nbDisponibles;
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

    public Integer getNbExemplaires() {
        return nbExemplaires;
    }

    public void setNbExemplaires(Integer nbExemplaires) {
        this.nbExemplaires = nbExemplaires;
    }

    public Integer getNbDisponibles() {
        return nbDisponibles;
    }

    public void setNbDisponibles(Integer nbDisponibles) {
        this.nbDisponibles = nbDisponibles;
    }
}
