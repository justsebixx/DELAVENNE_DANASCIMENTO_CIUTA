package com.example.bibliotheque_quali_dev.exception;

public class LivreNotAvailableException extends RuntimeException {
    
    public LivreNotAvailableException(Integer id) {
        super("Livre non disponible avec l'ID : " + id);
    }
    
    public LivreNotAvailableException(String message) {
        super(message);
    }
    
    public LivreNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
