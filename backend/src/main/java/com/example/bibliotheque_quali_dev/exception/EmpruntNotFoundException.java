package com.example.bibliotheque_quali_dev.exception;

public class EmpruntNotFoundException extends RuntimeException {
    
    public EmpruntNotFoundException(Integer id) {
        super("Emprunt non trouvé avec l'ID : " + id);
    }
    
    public EmpruntNotFoundException(String message) {
        super(message);
    }
    
    public EmpruntNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
