package com.example.bibliotheque_quali_dev.exception;

public class UtilisateurAlreadyExistsException extends RuntimeException {
    
    public UtilisateurAlreadyExistsException(String email) {
        super("Un utilisateur avec l'email " + email + " existe déjà");
    }
    
    public UtilisateurAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
