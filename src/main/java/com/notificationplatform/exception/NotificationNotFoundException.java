package com.notificationplatform.exception;

/**
 * Thrown when a requested notification cannot be found by its identifier.
 */
public class NotificationNotFoundException extends RuntimeException {

    public NotificationNotFoundException(String id) {
        super("Notification not found with id: " + id);
    }
}
