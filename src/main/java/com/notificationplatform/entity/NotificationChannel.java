package com.notificationplatform.entity;

/**
 * Supported delivery channels for notifications.
 *
 * <p>Using an enum provides:
 * <ul>
 *   <li>Type safety (prevents arbitrary strings like "mail" or "e-mail")</li>
 *   <li>Compile-time validation</li>
 *   <li>Single source of truth for all delivery channels</li>
 * </ul>
 */
public enum NotificationChannel {
    EMAIL,
    SMS,
    PUSH
}
