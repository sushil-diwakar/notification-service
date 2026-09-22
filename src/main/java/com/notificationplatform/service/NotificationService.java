package com.notificationplatform.service;

import com.notificationplatform.dto.CreateNotificationRequest;
import com.notificationplatform.dto.NotificationResponse;
import com.notificationplatform.entity.Notification;
import com.notificationplatform.entity.NotificationStatus;
import com.notificationplatform.exception.NotificationNotFoundException;
import com.notificationplatform.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service orchestrating notification business operations.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Coordinates creation of new notification domain models</li>
 *   <li>Assigns business identifiers and initial status</li>
 *   <li>Interacts with the persistence layer via {@link NotificationRepository}</li>
 *   <li>Maps internal domain models to external response DTOs</li>
 * </ul>
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;

    /**
     * Constructor injection ensures immutability, thread-safety,
     * and effortless testability with test doubles.
     */
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    /**
     * Creates and records a new notification request.
     *
     * @param request validated request containing notification parameters
     * @return response representing the created notification with generated ID and CREATED status
     */
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        String id = UUID.randomUUID().toString();
        Instant now = Instant.now();

        Notification notification = new Notification(
                id,
                request.recipient(),
                request.channel(),
                request.subject(),
                request.message(),
                NotificationStatus.CREATED,
                now,
                now
        );

        Notification saved = notificationRepository.save(notification);
        log.info("Created notification with id={} for recipient={} via channel={}",
                saved.getId(), saved.getRecipient(), saved.getChannel());

        return NotificationResponse.from(saved);
    }

    /**
     * Retrieves a notification by its unique identifier.
     *
     * @param id notification identifier
     * @return response DTO
     * @throws NotificationNotFoundException if no notification matches the ID
     */
    public NotificationResponse getNotificationById(String id) {
        return notificationRepository.findById(id)
                .map(NotificationResponse::from)
                .orElseThrow(() -> new NotificationNotFoundException(id));
    }

    /**
     * Retrieves all recorded notifications.
     *
     * @return list of notification response DTOs
     */
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }
}
