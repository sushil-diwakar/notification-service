package com.notificationplatform.repository;

import com.notificationplatform.entity.Notification;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing Notification persistence.
 *
 * <p>Following the Dependency Inversion Principle (DIP), higher-level modules
 * (such as {@link com.notificationplatform.service.NotificationService}) depend on this
 * abstraction rather than a specific persistence technology.
 *
 * In Phase 1, an in-memory implementation is used.
 * In later phases, a Spring Data JPA implementation backed by MySQL will be swapped in.
 */
public interface NotificationRepository {

    Notification save(Notification notification);

    Optional<Notification> findById(String id);

    List<Notification> findAll();
}
