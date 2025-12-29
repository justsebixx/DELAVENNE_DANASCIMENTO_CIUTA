package com.example.bibliotheque_quali_dev.dto;

import java.util.List;

public class DashboardResponse {
    private long totalEmprunts;
    private long empruntsEnCours;
    private long empruntsEnRetard;
    private double tauxRetard;
    private List<TopLivreStat> topLivres;

    public DashboardResponse() {
    }

    public DashboardResponse(long totalEmprunts, long empruntsEnCours, long empruntsEnRetard, double tauxRetard, List<TopLivreStat> topLivres) {
        this.totalEmprunts = totalEmprunts;
        this.empruntsEnCours = empruntsEnCours;
        this.empruntsEnRetard = empruntsEnRetard;
        this.tauxRetard = tauxRetard;
        this.topLivres = topLivres;
    }

    public long getTotalEmprunts() {
        return totalEmprunts;
    }

    public void setTotalEmprunts(long totalEmprunts) {
        this.totalEmprunts = totalEmprunts;
    }

    public long getEmpruntsEnCours() {
        return empruntsEnCours;
    }

    public void setEmpruntsEnCours(long empruntsEnCours) {
        this.empruntsEnCours = empruntsEnCours;
    }

    public long getEmpruntsEnRetard() {
        return empruntsEnRetard;
    }

    public void setEmpruntsEnRetard(long empruntsEnRetard) {
        this.empruntsEnRetard = empruntsEnRetard;
    }

    public double getTauxRetard() {
        return tauxRetard;
    }

    public void setTauxRetard(double tauxRetard) {
        this.tauxRetard = tauxRetard;
    }

    public List<TopLivreStat> getTopLivres() {
        return topLivres;
    }

    public void setTopLivres(List<TopLivreStat> topLivres) {
        this.topLivres = topLivres;
    }
}
