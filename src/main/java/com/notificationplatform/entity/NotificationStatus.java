package com.notificationplatform.entity;

/**
 * Lifecycle states of a notification in the platform.
 *
 * <p>State transitions:
 * <pre>
 *   CREATED ──► PROCESSING ──► SENT
 *                    │
 *                    └──► FAILED
 * </pre>
 *
 * In Phase 1, notifications start in the {@code CREATED} state upon receipt.
 */
public enum NotificationStatus {
    CREATED,
    PROCESSING,
    SENT,
    FAILED
}
