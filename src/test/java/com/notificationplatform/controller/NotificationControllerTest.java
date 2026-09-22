package com.notificationplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationplatform.dto.CreateNotificationRequest;
import com.notificationplatform.dto.NotificationResponse;
import com.notificationplatform.entity.NotificationChannel;
import com.notificationplatform.entity.NotificationStatus;
import com.notificationplatform.exception.GlobalExceptionHandler;
import com.notificationplatform.exception.NotificationNotFoundException;
import com.notificationplatform.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * WebMvc slice tests for {@link NotificationController}.
 *
 * <p>Validates HTTP status codes, JSON serialization/deserialization,
 * input validation, Location header, and error handling through {@link GlobalExceptionHandler}.
 */
@WebMvcTest(NotificationController.class)
@Import(GlobalExceptionHandler.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationService notificationService;

    @Test
    @DisplayName("POST /api/v1/notifications with valid payload returns 201 Created and Location header")
    void createNotification_success() throws Exception {
        CreateNotificationRequest request = new CreateNotificationRequest(
                "user@example.com",
                NotificationChannel.EMAIL,
                "Welcome",
                "Welcome to our platform"
        );

        NotificationResponse response = new NotificationResponse(
                "notif-123",
                "user@example.com",
                NotificationChannel.EMAIL,
                "Welcome",
                "Welcome to our platform",
                NotificationStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        when(notificationService.createNotification(any(CreateNotificationRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/v1/notifications/notif-123")))
                .andExpect(jsonPath("$.id").value("notif-123"))
                .andExpect(jsonPath("$.recipient").value("user@example.com"))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.subject").value("Welcome"))
                .andExpect(jsonPath("$.message").value("Welcome to our platform"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/notifications with blank recipient returns 400 Bad Request with VALIDATION_ERROR")
    void createNotification_blankRecipient_returns400() throws Exception {
        String invalidPayload = """
                {
                    "recipient": "",
                    "channel": "EMAIL",
                    "subject": "Welcome",
                    "message": "Welcome to our platform"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("recipient is required")))
                .andExpect(jsonPath("$.path").value("/api/v1/notifications"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }

    @Test
    @DisplayName("POST /api/v1/notifications with null channel returns 400 Bad Request with VALIDATION_ERROR")
    void createNotification_nullChannel_returns400() throws Exception {
        String invalidPayload = """
                {
                    "recipient": "user@example.com",
                    "subject": "Welcome",
                    "message": "Welcome to our platform"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("channel is required")));
    }

    @Test
    @DisplayName("POST /api/v1/notifications with blank message returns 400 Bad Request with VALIDATION_ERROR")
    void createNotification_blankMessage_returns400() throws Exception {
        String invalidPayload = """
                {
                    "recipient": "user@example.com",
                    "channel": "SMS",
                    "message": "   "
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message", containsString("message is required")));
    }

    @Test
    @DisplayName("POST /api/v1/notifications with invalid channel enum returns 400 Bad Request with INVALID_PAYLOAD")
    void createNotification_invalidChannelEnum_returns400() throws Exception {
        String invalidPayload = """
                {
                    "recipient": "user@example.com",
                    "channel": "CARRIER_PIGEON",
                    "message": "Hello!"
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("INVALID_PAYLOAD"));
    }

    @Test
    @DisplayName("GET /api/v1/notifications/{id} with existing ID returns 200 OK")
    void getNotificationById_existing_returns200() throws Exception {
        String id = "notif-abc";
        NotificationResponse response = new NotificationResponse(
                id,
                "+1234567890",
                NotificationChannel.SMS,
                null,
                "Your security code is 987654",
                NotificationStatus.CREATED,
                Instant.now(),
                Instant.now()
        );

        when(notificationService.getNotificationById(id)).thenReturn(response);

        mockMvc.perform(get("/api/v1/notifications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.recipient").value("+1234567890"))
                .andExpect(jsonPath("$.channel").value("SMS"))
                .andExpect(jsonPath("$.message").value("Your security code is 987654"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    @DisplayName("GET /api/v1/notifications/{id} with non-existing ID returns 404 Not Found")
    void getNotificationById_nonExisting_returns404() throws Exception {
        String id = "notif-missing";
        when(notificationService.getNotificationById(id))
                .thenThrow(new NotificationNotFoundException(id));

        mockMvc.perform(get("/api/v1/notifications/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Notification not found with id: " + id))
                .andExpect(jsonPath("$.path").value("/api/v1/notifications/" + id));
    }

    @Test
    @DisplayName("GET /api/v1/notifications returns 200 OK with list of notifications")
    void getAllNotifications_returnsList() throws Exception {
        NotificationResponse r1 = new NotificationResponse(
                "id-1", "user1@example.com", NotificationChannel.EMAIL, "Subject 1", "Message 1",
                NotificationStatus.CREATED, Instant.now(), Instant.now()
        );
        NotificationResponse r2 = new NotificationResponse(
                "id-2", "user2@example.com", NotificationChannel.PUSH, null, "Message 2",
                NotificationStatus.SENT, Instant.now(), Instant.now()
        );

        when(notificationService.getAllNotifications()).thenReturn(List.of(r1, r2));

        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value("id-1"))
                .andExpect(jsonPath("$[1].id").value("id-2"));
    }
}
