package com.example.bibliotheque_quali_dev.exception;

public class UtilisateurNotFoundException extends RuntimeException {
    
    public UtilisateurNotFoundException(Integer id) {
        super("Utilisateur non trouvé avec l'ID : " + id);
    }
    
    public UtilisateurNotFoundException(String message) {
        super(message);
    }
    
    public UtilisateurNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
