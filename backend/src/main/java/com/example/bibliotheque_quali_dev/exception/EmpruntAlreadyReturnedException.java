package com.example.bibliotheque_quali_dev.exception;

public class EmpruntAlreadyReturnedException extends RuntimeException {
    
    public EmpruntAlreadyReturnedException(Integer id) {
        super("L'emprunt avec l'ID " + id + " a déjà été retourné");
    }
    
    public EmpruntAlreadyReturnedException(String message) {
        super(message);
    }
    
    public EmpruntAlreadyReturnedException(String message, Throwable cause) {
        super(message, cause);
    }
}
