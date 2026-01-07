package com.example.bibliotheque_quali_dev.dto;

public class TopLivreStat {
    private Integer idLivre;
    private String titre;
    private long nbEmprunts;

    public TopLivreStat() {
    }

    public TopLivreStat(Integer idLivre, String titre, int nbEmprunts) {
        this.idLivre = idLivre;
        this.titre = titre;
        this.nbEmprunts = nbEmprunts;
    }

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

    public long getNbEmprunts() {
        return nbEmprunts;
    }

    public void setNbEmprunts(long nbEmprunts) {
        this.nbEmprunts = nbEmprunts;
    }
}
