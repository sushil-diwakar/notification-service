# Distributed Notification Platform

> **Current Phase: Phase 0 — Project Bootstrap**

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

## Current Architecture (Phase 0)

```
Client (browser / curl / Postman)
          │
          │  HTTP GET /api/v1/health
          ▼
  ┌─────────────────────────────┐
  │   Spring Boot REST API      │
  │                             │
  │   HealthController          │
  │   └── GET /api/v1/health    │
  └─────────────────────────────┘
```

At this stage the application is intentionally minimal. There is no database, no message broker, and no business logic. The goal is a working, tested, properly structured foundation.

---

## Technology Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 (LTS) | Language runtime |
| Spring Boot | 3.3.x | Application framework |
| Maven | 3.9+ | Build tool & dependency management |
| Spring Web | (via Boot) | REST API, embedded Tomcat |
| Spring Boot Actuator | (via Boot) | Production-grade operations endpoints |
| JUnit 5 | (via Boot) | Unit & integration testing |
| Mockito | (via Boot) | Test doubles (mocks/stubs) |

### Coming in future phases

| Technology | Phase | Purpose |
|------------|-------|---------|
| MySQL + Spring Data JPA | Phase 1 | Persistent notification storage |
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

# Run tests with verbose output
mvn test -Dsurefire.failIfNoSpecifiedTests=false
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
│   │   ├── controller/                            # HTTP layer — receives requests
│   │   │   └── HealthController.java
│   │   ├── service/                               # Business logic layer (Phase 1+)
│   │   ├── repository/                            # Data access layer (Phase 1+)
│   │   ├── entity/                                # JPA entities / DB models (Phase 1+)
│   │   ├── dto/                                   # Request/response data shapes
│   │   │   └── HealthResponse.java
│   │   ├── exception/                             # Custom exceptions & error handling (Phase 1+)
│   │   ├── config/                                # Spring @Configuration classes (Phase 1+)
│   │   └── common/                                # Shared utilities (Phase 1+)
│   └── resources/
│       └── application.properties                 # Externalized configuration
└── test/
    └── java/com/notificationplatform/
        ├── NotificationPlatformApplicationTest.java  # Context smoke test
        └── controller/
            └── HealthControllerTest.java             # Endpoint tests
```

---

## Configuration

All configuration is externalized in `src/main/resources/application.properties`.

**No secrets, passwords, or API keys are committed to this repository.**

To override any property at runtime:

```bash
# Via command-line argument
java -jar app.jar --server.port=9090

# Via environment variable (Spring converts SERVER_PORT → server.port)
SERVER_PORT=9090 java -jar app.jar
```

---

## Development Phases

| Phase | Goal | Status |
|-------|------|--------|
| **Phase 0** | Project bootstrap, health endpoint | ✅ Complete |
| Phase 1 | Notification domain model, JPA, MySQL | ⏳ Planned |
| Phase 2 | Notification service, REST CRUD API | ⏳ Planned |
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
