package com.example.bibliotheque_quali_dev.exception;

public class NotificationNotFoundException extends RuntimeException {
    
    public NotificationNotFoundException(Integer id) {
        super("Notification non trouvée avec l'ID : " + id);
    }
    
    public NotificationNotFoundException(String message) {
        super(message);
    }
    
    public NotificationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
