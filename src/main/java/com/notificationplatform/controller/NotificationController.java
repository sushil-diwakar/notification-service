package com.notificationplatform.controller;

import com.notificationplatform.dto.CreateNotificationRequest;
import com.notificationplatform.dto.NotificationResponse;
import com.notificationplatform.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for submitting and querying notification requests.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>{@code POST /api/v1/notifications} - submit a notification request</li>
 *   <li>{@code GET /api/v1/notifications/{id}} - retrieve notification by ID</li>
 *   <li>{@code GET /api/v1/notifications} - list all notification requests</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * Submits a new notification request.
     *
     * @param request validated notification request payload
     * @return 201 Created with Location header and response body
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(
            @Valid @RequestBody CreateNotificationRequest request
    ) {
        NotificationResponse response = notificationService.createNotification(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves a notification request by ID.
     *
     * @param id the unique notification identifier
     * @return 200 OK with the notification response
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable String id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all recorded notification requests.
     *
     * @return 200 OK with list of notification responses
     */
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications() {
        List<NotificationResponse> responses = notificationService.getAllNotifications();
        return ResponseEntity.ok(responses);
    }
}
