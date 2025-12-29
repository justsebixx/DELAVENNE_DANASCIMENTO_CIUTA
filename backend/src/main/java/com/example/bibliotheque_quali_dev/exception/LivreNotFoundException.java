package com.example.bibliotheque_quali_dev.exception;

public class LivreNotFoundException extends RuntimeException {
    
    public LivreNotFoundException(Integer id) {
        super("Livre non trouvé avec l'ID : " + id);
    }
    
    public LivreNotFoundException(String message) {
        super(message);
    }
    
    public LivreNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
