package com.notificationplatform.dto;

import com.notificationplatform.entity.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request payload for creating a new notification.
 *
 * <p>Uses Jakarta Bean Validation annotations to enforce input constraints
 * at the HTTP boundary before request data reaches the service layer.
 */
public record CreateNotificationRequest(
        @NotBlank(message = "recipient is required")
        String recipient,

        @NotNull(message = "channel is required")
        NotificationChannel channel,

        String subject,

        @NotBlank(message = "message is required")
        String message
) {}
