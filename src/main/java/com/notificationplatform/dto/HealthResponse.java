package com.notificationplatform.dto;

/**
 * Data Transfer Object (DTO) representing the health check response.
 *
 * <p><b>What is a DTO?</b><br>
 * A DTO is a simple object that carries data between layers or across
 * the network. It has:
 * <ul>
 *   <li>No business logic</li>
 *   <li>No database annotations</li>
 *   <li>Only fields, constructors, and accessors</li>
 * </ul>
 *
 * <p><b>Why use a DTO instead of returning a raw Map or String?</b>
 * <ul>
 *   <li>Type-safety: the compiler catches mistakes</li>
 *   <li>Readability: the response contract is documented in code</li>
 *   <li>Evolvability: easy to add/remove fields in one place</li>
 *   <li>Testability: you can assert on specific fields in tests</li>
 * </ul>
 *
 * <p><b>Why a Java record?</b><br>
 * Java records (introduced in Java 16, finalized) give us an immutable
 * data class with auto-generated constructor, accessors, equals, hashCode,
 * and toString — all in one line. Perfect for DTOs.
 *
 * <p>Jackson (the JSON library Spring Boot uses) natively supports records.
 *
 * @param status    UP/DOWN status string
 * @param message   Human-readable description
 * @param timestamp ISO-8601 timestamp of when the health was checked
 */
public record HealthResponse(
        String status,
        String message,
        String timestamp
) {}
