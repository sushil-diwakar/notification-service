package com.notificationplatform.repository;

import com.notificationplatform.entity.Notification;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe, in-memory implementation of {@link NotificationRepository}.
 *
 * <p>Spring web requests are handled concurrently across multiple threads.
 * Using {@link ConcurrentHashMap} ensures safe concurrent reads and writes
 * without race conditions or corrupt state during Phase 1.
 */
@Repository
public class InMemoryNotificationRepository implements NotificationRepository {

    private final Map<String, Notification> storage = new ConcurrentHashMap<>();

    @Override
    public Notification save(Notification notification) {
        storage.put(notification.getId(), notification);
        return notification;
    }

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Notification> findAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Clears the in-memory storage. Useful for testing or resetting state.
     */
    public void clear() {
        storage.clear();
    }
}
