package com.example.bibliotheque_quali_dev.entity;

/**
 * Énumération des rôles utilisateur dans le système.
 * Définit les niveaux d'accès et les permissions associées.
 */
public enum Role {
    /**
     * Étudiant - Accès de base
     * Limite : 3 emprunts simultanés
     */
    ETUDIANT,
    
    /**
     * Enseignant - Accès étendu
     * Limite : 5 emprunts simultanés
     */
    ENSEIGNANT,
    
    /**
     * Bibliothécaire - Gestion complète de la bibliothèque
     * Limite : 10 emprunts simultanés
     * Permissions : CRUD livres, gestion emprunts, accès dashboard
     */
    BIBLIOTHECAIRE,
    
    /**
     * Administrateur - Accès total
     * Permissions : Toutes + gestion utilisateurs
     */
    ADMIN;
    
    /**
     * Retourne la limite d'emprunts autorisés pour ce rôle
     * @return nombre maximum d'emprunts simultanés
     */
    public int getLimiteEmprunts() {
        return switch (this) {
            case ETUDIANT -> 3;
            case ENSEIGNANT -> 5;
            case BIBLIOTHECAIRE -> 10;
            case ADMIN -> 10;
        };
    }
}
