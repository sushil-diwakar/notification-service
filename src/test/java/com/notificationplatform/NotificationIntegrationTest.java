package com.notificationplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notificationplatform.dto.CreateNotificationRequest;
import com.notificationplatform.entity.NotificationChannel;
import com.notificationplatform.entity.NotificationStatus;
import com.notificationplatform.repository.InMemoryNotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * End-to-end integration test validating the entire notification workflow
 * through the Spring Boot application context (Controller -> Service -> Repository).
 */
@SpringBootTest
@AutoConfigureMockMvc
class NotificationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InMemoryNotificationRepository inMemoryRepository;

    @BeforeEach
    void setUp() {
        inMemoryRepository.clear();
    }

    @Test
    @DisplayName("End-to-End: Create, retrieve by ID, and list notifications")
    void fullNotificationLifecycle() throws Exception {
        // 1. Create notification
        CreateNotificationRequest request = new CreateNotificationRequest(
                "john.doe@example.com",
                NotificationChannel.EMAIL,
                "Account Activation",
                "Please click the link to activate your account."
        );

        String postResponseContent = mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.recipient").value("john.doe@example.com"))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String notificationId = objectMapper.readTree(postResponseContent).get("id").asText();

        // 2. Retrieve by ID
        mockMvc.perform(get("/api/v1/notifications/{id}", notificationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(notificationId))
                .andExpect(jsonPath("$.recipient").value("john.doe@example.com"))
                .andExpect(jsonPath("$.channel").value("EMAIL"))
                .andExpect(jsonPath("$.message").value("Please click the link to activate your account."))
                .andExpect(jsonPath("$.status").value("CREATED"));

        // 3. List all notifications
        mockMvc.perform(get("/api/v1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(notificationId));

        // 4. Query non-existent ID
        mockMvc.perform(get("/api/v1/notifications/{id}", "unknown-id-999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("NOT_FOUND"));
    }
}
