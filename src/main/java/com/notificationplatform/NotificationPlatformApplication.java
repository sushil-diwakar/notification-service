package com.notificationplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Notification Platform application.
 *
 * <p>@SpringBootApplication is a convenience annotation that combines:
 * <ul>
 *   <li>@Configuration     — marks this class as a source of bean definitions</li>
 *   <li>@EnableAutoConfiguration — tells Spring Boot to start adding beans based
 *       on classpath settings, other beans, and various property settings</li>
 *   <li>@ComponentScan    — tells Spring to look for components, configurations,
 *       and services in the com.notificationplatform package and its sub-packages</li>
 * </ul>
 *
 * <p><b>Inversion of Control (IoC):</b> Instead of your code creating objects
 * with {@code new}, Spring creates and manages them. You just declare what you
 * need and Spring "injects" it — this is Dependency Injection (DI).
 */
@SpringBootApplication
public class NotificationPlatformApplication {

    public static void main(String[] args) {
        // SpringApplication.run() bootstraps the entire application:
        // 1. Creates the ApplicationContext (the IoC container)
        // 2. Registers all beans (@Component, @Service, @RestController, etc.)
        // 3. Starts the embedded Tomcat server
        // 4. Opens the HTTP port defined in application.properties
        SpringApplication.run(NotificationPlatformApplication.class, args);
    }
}
