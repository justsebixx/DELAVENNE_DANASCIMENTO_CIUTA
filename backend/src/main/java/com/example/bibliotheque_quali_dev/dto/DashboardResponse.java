package com.example.bibliotheque_quali_dev.dto;

import java.util.List;

public class DashboardResponse {
    private int totalLivres;
    private int livresDisponibles;
    private int livresEmpruntes;
    private int totalUtilisateurs;
    private int empruntsEnCours;
    private int empruntsEnRetard;
    private List<TopLivreStat> topLivres;

    public DashboardResponse() {
    }

    public int getTotalLivres() {
        return totalLivres;
    }

    public void setTotalLivres(int totalLivres) {
        this.totalLivres = totalLivres;
    }

    public int getLivresDisponibles() {
        return livresDisponibles;
    }

    public void setLivresDisponibles(int livresDisponibles) {
        this.livresDisponibles = livresDisponibles;
    }

    public int getLivresEmpruntes() {
        return livresEmpruntes;
    }

    public void setLivresEmpruntes(int livresEmpruntes) {
        this.livresEmpruntes = livresEmpruntes;
    }

    public int getTotalUtilisateurs() {
        return totalUtilisateurs;
    }

    public void setTotalUtilisateurs(int totalUtilisateurs) {
        this.totalUtilisateurs = totalUtilisateurs;
    }

    public int getEmpruntsEnCours() {
        return empruntsEnCours;
    }

    public void setEmpruntsEnCours(int empruntsEnCours) {
        this.empruntsEnCours = empruntsEnCours;
    }

    public int getEmpruntsEnRetard() {
        return empruntsEnRetard;
    }

    public void setEmpruntsEnRetard(int empruntsEnRetard) {
        this.empruntsEnRetard = empruntsEnRetard;
    }

    public List<TopLivreStat> getTopLivres() {
        return topLivres;
    }

    public void setTopLivres(List<TopLivreStat> topLivres) {
        this.topLivres = topLivres;
    }
}
