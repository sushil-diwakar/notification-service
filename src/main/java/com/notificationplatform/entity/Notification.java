package com.notificationplatform.entity;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain model representing a Notification.
 *
 * <p>This class encapsulates the core business data and identity of a notification.
 * In later phases, this class will be annotated as a JPA entity mapped to a relational table.
 * For now, it serves as a pure domain model with clear boundaries and business rules.
 */
public class Notification {

    private final String id;
    private final String recipient;
    private final NotificationChannel channel;
    private final String subject;
    private final String message;
    private NotificationStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    public Notification(
            String id,
            String recipient,
            NotificationChannel channel,
            String subject,
            String message,
            NotificationStatus status,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.recipient = Objects.requireNonNull(recipient, "recipient must not be null");
        this.channel = Objects.requireNonNull(channel, "channel must not be null");
        this.subject = subject;
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    public String getId() {
        return id;
    }

    public String getRecipient() {
        return recipient;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateStatus(NotificationStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "newStatus must not be null");
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id='" + id + '\'' +
                ", recipient='" + recipient + '\'' +
                ", channel=" + channel +
                ", subject='" + subject + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
