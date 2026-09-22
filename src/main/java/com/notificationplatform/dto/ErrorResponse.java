package com.notificationplatform.dto;

import java.time.Instant;

/**
 * Standard error response structure returned across all API endpoints.
 *
 * <p>Provides consistent error diagnostics to API consumers:
 * <ul>
 *   <li>{@code timestamp}: ISO-8601 time when the error occurred</li>
 *   <li>{@code status}: HTTP status code matching the response headers</li>
 *   <li>{@code error}: High-level error classification code</li>
 *   <li>{@code message}: Human-readable error description or validation details</li>
 *   <li>{@code path}: Request URI that triggered the error</li>
 * </ul>
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {}
