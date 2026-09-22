package com.notificationplatform.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HealthController}.
 *
 * <p><b>@WebMvcTest vs @SpringBootTest:</b><br>
 * {@code @WebMvcTest(HealthController.class)} is a "slice test":
 * <ul>
 *   <li>It only loads the web layer (controllers, filters, etc.)</li>
 *   <li>It does NOT load services, repositories, or the full context</li>
 *   <li>It is significantly FASTER than @SpringBootTest</li>
 *   <li>It auto-configures MockMvc for you</li>
 * </ul>
 *
 * <p>Use {@code @WebMvcTest} for controller tests, and {@code @SpringBootTest}
 * when you need to test the full application stack.
 *
 * <p><b>MockMvc:</b><br>
 * MockMvc allows you to simulate HTTP requests without starting a real
 * HTTP server. Requests go through the full Spring MVC dispatch pipeline
 * (DispatcherServlet, interceptors, message converters) but stay in-process.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

    /**
     * Spring injects MockMvc because @WebMvcTest auto-configures it.
     * This is Dependency Injection in action — we declare what we need
     * and Spring provides it.
     */
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/v1/health returns 200 OK")
    void healthEndpoint_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andDo(print())                                          // logs full request/response to console
                .andExpect(status().isOk());                             // HTTP 200
    }

    @Test
    @DisplayName("GET /api/v1/health returns JSON content type")
    void healthEndpoint_returnsJson() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/v1/health response body contains status=UP")
    void healthEndpoint_bodyContainsStatusUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    @DisplayName("GET /api/v1/health response body contains a message")
    void healthEndpoint_bodyContainsMessage() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(jsonPath("$.message").isNotEmpty());
    }

    @Test
    @DisplayName("GET /api/v1/health response body contains a timestamp")
    void healthEndpoint_bodyContainsTimestamp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty());
    }
}
