package com.notificationplatform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Application context smoke test.
 *
 * <p><b>What does this test do?</b><br>
 * It verifies that the entire Spring ApplicationContext loads without errors.
 * This catches a wide class of bugs:
 * <ul>
 *   <li>Missing beans that are required by @Autowired fields</li>
 *   <li>Configuration errors in application.properties</li>
 *   <li>Circular dependency issues</li>
 *   <li>Missing @Component / @Service annotations</li>
 * </ul>
 *
 * <p><b>@SpringBootTest</b><br>
 * This annotation tells Spring Boot's test runner to:
 * <ol>
 *   <li>Start the full application context (all beans, all config)</li>
 *   <li>Use test-specific configuration if provided</li>
 * </ol>
 *
 * <p>By default, it does NOT start the embedded server (use
 * {@code webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT}
 * when you need to fire real HTTP requests).
 *
 * <p><b>Why have a test that only checks context loading?</b><br>
 * It is your fastest "is anything fundamentally broken?" guard.
 * It runs in every CI pipeline and gives instant feedback.
 */
@SpringBootTest
class NotificationPlatformApplicationTest {

    @Test
    @DisplayName("Application context loads successfully")
    void contextLoads() {
        // If the context fails to start, Spring throws an exception
        // and this test fails automatically — no assertion needed.
        // The test body is intentionally empty.
    }
}
