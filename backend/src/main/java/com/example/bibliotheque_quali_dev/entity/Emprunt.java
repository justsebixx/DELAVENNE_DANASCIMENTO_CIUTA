package com.example.bibliotheque_quali_dev.entity;
import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "emprunts")
public class Emprunt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEmprunt;
    private Integer idUser;
    private Integer idLivre;
    private Date dateEmprunt;
    private Date dateRetourPrevue;
    private Date dateRetourEffective;
    enum statut {en_cours,rendu,retard};

    @ManyToOne
    @JoinColumn(name = "idLivre", insertable = false, updatable = false)
    private Livre livre;

    public Emprunt() {
    }

    public Emprunt(Integer idEmprunt, Integer idUser, Integer idLivre, Date dateEmprunt, Date dateRetourPrevue, Date dateRetourEffective) {
        this.idEmprunt = idEmprunt;
        this.idUser = idUser;
        this.idLivre = idLivre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
    }

    public Integer getIdEmprunt() {
        return idEmprunt;
    }

    public void setIdEmprunt(Integer idEmprunt) {
        this.idEmprunt = idEmprunt;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Integer getIdLivre() {
        return idLivre;
    }

    public void setIdLivre(Integer idLivre) {
        this.idLivre = idLivre;
    }

    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public Date getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public void setDateRetourPrevue(Date dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public Date getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(Date dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }
}
