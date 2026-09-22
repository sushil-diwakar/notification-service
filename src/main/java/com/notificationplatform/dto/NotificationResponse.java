package com.notificationplatform.dto;

import com.notificationplatform.entity.Notification;
import com.notificationplatform.entity.NotificationChannel;
import com.notificationplatform.entity.NotificationStatus;

import java.time.Instant;

/**
 * Response payload representing a notification returned by the API.
 *
 * <p>Decouples internal domain representations from the external API contract.
 */
public record NotificationResponse(
        String id,
        String recipient,
        NotificationChannel channel,
        String subject,
        String message,
        NotificationStatus status,
        Instant createdAt,
        Instant updatedAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getRecipient(),
                notification.getChannel(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getUpdatedAt()
        );
    }
}
