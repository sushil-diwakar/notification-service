# Distributed Notification Platform

> **Current Phase: Phase 1 — Notification REST API**

A production-style backend system for delivering notifications across multiple channels (Email, SMS, Push). Built incrementally, phase by phase, following clean architecture and SOLID principles.

---

## Project Purpose

This platform accepts notification requests and (eventually) delivers them asynchronously through:

| Channel | Description |
|---------|-------------|
| EMAIL   | Transactional and marketing emails |
| SMS     | Text message alerts |
| PUSH    | Mobile/web push notifications |

The final architecture will use **Apache Kafka** to decouple notification creation from delivery, enabling high throughput and fault tolerance. However, we build up to that deliberately — one phase at a time.

---

## Current Architecture (Phase 1)

```
Client (browser / curl / Postman)
          │
          │  HTTP POST /api/v1/notifications
          │  HTTP GET  /api/v1/notifications/{id}
          │  HTTP GET  /api/v1/notifications
          │  HTTP GET  /api/v1/health
          ▼
  ┌─────────────────────────────────────────────────────────────┐
  │                   Spring Boot REST API                      │
  │                                                             │
  │   HealthController          NotificationController          │
  │   └── GET /api/v1/health    ├── POST /api/v1/notifications  │
  │                             ├── GET  /api/v1/notifications/{id}
  │                             └── GET  /api/v1/notifications  │
  │                                           │                 │
  │                                           ▼                 │
  │                                  NotificationService        │
  │                                           │                 │
  │                                           ▼                 │
  │                              NotificationRepository         │
  │                                  (In-Memory Store)          │
  └─────────────────────────────────────────────────────────────┘
```

In Phase 1, the core notification domain and REST API are fully functional with in-memory persistence and Bean Validation.

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 (LTS) | Language runtime |
| Spring Boot | 3.3.x | Application framework |
| Maven | 3.9+ | Build tool & dependency management |
| Spring Web | (via Boot) | REST API, embedded Tomcat |
| Spring Boot Validation | (via Boot) | Jakarta Bean Validation (Hibernate Validator) |
| Spring Boot Actuator | (via Boot) | Production-grade operations endpoints |
| JUnit 5 | (via Boot) | Unit & integration testing |
| Mockito | (via Boot) | Test doubles (mocks/stubs) |

### Coming in future phases

| Technology | Phase | Purpose |
|------------|-------|---------|
| MySQL + Spring Data JPA | Phase 2 | Persistent notification storage |
| Kafka | Phase 3 | Async notification delivery |
| Redis | Phase 4 | Caching & rate limiting |
| Spring Security + JWT | Phase 5 | Authentication & authorization |
| Docker / Docker Compose | Phase 6 | Containerization |
| Prometheus + Grafana | Phase 7 | Metrics & dashboards |

---

## How to Run Locally

### Prerequisites

- Java 21 JDK installed ([Download](https://adoptium.net/))
- Maven 3.9+ installed ([Download](https://maven.apache.org/download.cgi))
- Verify: `java -version` and `mvn -version`

### Run the application

```bash
# Clone and enter the project
cd notification-system

# Compile and run
mvn spring-boot:run
```

The application starts on **http://localhost:8080**

### Test the health endpoint

```bash
curl http://localhost:8080/api/v1/health
```

Expected response:
```json
{
  "status": "UP",
  "message": "Notification Platform is running",
  "timestamp": "2024-01-15T10:30:00.123Z"
}
```

### Submit a notification request (Phase 1)

```bash
curl -X POST http://localhost:8080/api/v1/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "recipient": "user@example.com",
    "channel": "EMAIL",
    "subject": "Welcome",
    "message": "Welcome to our platform"
  }'
```

Expected response (`201 Created` with `Location` header):
```json
{
  "id": "7b8e5c1e-8e5e-4a67-938c-8f15b81a28a3",
  "recipient": "user@example.com",
  "channel": "EMAIL",
  "subject": "Welcome",
  "message": "Welcome to our platform",
  "status": "CREATED",
  "createdAt": "2026-09-23T02:30:00.000Z",
  "updatedAt": "2026-09-23T02:30:00.000Z"
}
```

### Retrieve a notification by ID

```bash
curl http://localhost:8080/api/v1/notifications/7b8e5c1e-8e5e-4a67-938c-8f15b81a28a3
```

### List all notifications

```bash
curl http://localhost:8080/api/v1/notifications
```

### Spring Actuator endpoints (infrastructure health)

```bash
curl http://localhost:8080/actuator/health
curl http://localhost:8080/actuator/info
```

---

## How to Run Tests

```bash
# Run all tests
mvn test
```

---

## How to Build a Production JAR

```bash
mvn clean package

# Run the fat JAR
java -jar target/notification-platform-0.0.1-SNAPSHOT.jar
```

---

## Project Structure

```
src/
├── main/
│   ├── java/com/notificationplatform/
│   │   ├── NotificationPlatformApplication.java   # Entry point
│   │   ├── controller/                            # REST controllers (HTTP boundary)
│   │   │   ├── HealthController.java
│   │   │   └── NotificationController.java
│   │   ├── service/                               # Business logic & orchestration
│   │   │   └── NotificationService.java
│   │   ├── repository/                            # Data access boundary
│   │   │   ├── NotificationRepository.java        # Interface (DIP)
│   │   │   └── InMemoryNotificationRepository.java# Phase 1 In-Memory impl
│   │   ├── entity/                                # Domain models & enums
│   │   │   ├── Notification.java
│   │   │   ├── NotificationChannel.java
│   │   │   └── NotificationStatus.java
│   │   ├── dto/                                   # Request & response data shapes
│   │   │   ├── CreateNotificationRequest.java
│   │   │   ├── NotificationResponse.java
│   │   │   ├── ErrorResponse.java
│   │   │   └── HealthResponse.java
│   │   ├── exception/                             # Custom exceptions & global handler
│   │   │   ├── NotificationNotFoundException.java
│   │   │   └── GlobalExceptionHandler.java
│   │   ├── config/                                # Spring configuration (future phases)
│   │   └── common/                                # Shared utilities
│   └── resources/
│       └── application.yml                        # Externalized configuration
└── test/
    └── java/com/notificationplatform/
        ├── NotificationPlatformApplicationTest.java  # Context smoke test
        ├── NotificationIntegrationTest.java          # End-to-end integration test
        ├── controller/
        │   ├── HealthControllerTest.java             # Health endpoint slice test
        │   └── NotificationControllerTest.java       # Notification API slice tests
        └── service/
            └── NotificationServiceTest.java          # Service business logic unit tests
```

---

## Configuration

All configuration is externalized in `src/main/resources/application.yml`.

**No secrets, passwords, or API keys are committed to this repository.**

---

## Development Phases

| Phase | Goal | Status |
|-------|------|--------|
| **Phase 0** | Project bootstrap, health endpoint | ✅ Complete |
| **Phase 1** | Notification domain model, in-memory repo, REST API, validation | ✅ Complete |
| Phase 2 | MySQL persistence, Spring Data JPA | ⏳ Planned |
| Phase 3 | Kafka integration, async delivery | ⏳ Planned |
| Phase 4 | Redis caching, rate limiting | ⏳ Planned |
| Phase 5 | Spring Security, JWT authentication | ⏳ Planned |
| Phase 6 | Docker, Docker Compose | ⏳ Planned |
| Phase 7 | Prometheus, Grafana, observability | ⏳ Planned |

---

## Git Conventions

| Type | Usage |
|------|-------|
| `feat:` | New feature |
| `fix:` | Bug fix |
| `test:` | Adding tests |
| `docs:` | Documentation |
| `refactor:` | Code restructuring without behavior change |
| `chore:` | Build, deps, config changes |

Example: `feat(phase1): add notification entity and JPA repository`
