package com.notificationplatform.controller;

import com.notificationplatform.dto.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * Health check controller exposing a simple diagnostic endpoint.
 *
 * <p><b>Why a custom health endpoint alongside Spring Actuator?</b><br>
 * Spring Actuator's {@code /actuator/health} is for infrastructure tooling
 * (load balancers, Kubernetes liveness probes, monitoring systems).
 * Our {@code /api/v1/health} is an application-level endpoint that:
 * <ul>
 *   <li>Returns a domain-specific response structure we control</li>
 *   <li>Lives under our versioned API path ({@code /api/v1/...})</li>
 *   <li>Can later include application-specific health details
 *       (DB connectivity, queue depth, etc.)</li>
 * </ul>
 *
 * <p><b>@RestController</b> = @Controller + @ResponseBody.
 * Every method return value is automatically serialized to JSON
 * by Jackson (which Spring Boot auto-configures).
 *
 * <p><b>@RequestMapping("/api/v1")</b> sets the base path for all
 * endpoints in this controller, following REST versioning best practice.
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

    /**
     * Returns a simple health status response.
     *
     * <p>Using {@link ResponseEntity} gives us explicit control over:
     * <ul>
     *   <li>HTTP status code</li>
     *   <li>Response headers (if needed later)</li>
     *   <li>Response body</li>
     * </ul>
     *
     * @return 200 OK with a {@link HealthResponse} body
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        HealthResponse response = new HealthResponse(
                "UP",
                "Notification Platform is running",
                Instant.now().toString()
        );
        return ResponseEntity.ok(response);
    }
}
