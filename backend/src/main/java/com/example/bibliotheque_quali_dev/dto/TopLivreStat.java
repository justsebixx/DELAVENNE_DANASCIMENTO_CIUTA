package com.example.bibliotheque_quali_dev.dto;

public class TopLivreStat {
    private String titre;
    private long nbEmprunts;

    public TopLivreStat() {
    }

    public TopLivreStat(String titre, long nbEmprunts) {
        this.titre = titre;
        this.nbEmprunts = nbEmprunts;
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
