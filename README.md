# AI Agent Observatory — Backend

> **Backend API server for collecting and storing AI agent telemetry data.**
> Handles trace ingestion, span relationships, batch processing, and provides endpoints for querying agent performance metrics.

---

## Overview

The AI Agent Observatory Backend is a Spring Boot application that acts as the central telemetry store for AI agent observability. It integrates with the companion [Python SDK](https://github.com/DakshRJain737/ai-agent-observatory-sdk) to give full visibility into agent execution — traces, spans, token usage, cost, and latency — stored in PostgreSQL and served via a secured REST API.

```
Python Agent  ──[SDK]──►  Backend API  ──►  PostgreSQL
                                │
                                └──►  REST Endpoints  ──►  Dashboard / Query Tools
```

---

## Features

- **Trace Ingestion** — Create, query, and close execution traces per agent workflow
- **Span Management** — Record individual spans with parent/child relationships, model info, token usage, cost, and latency
- **Session Tracking** — Group traces by session and user for multi-turn agent workflows
- **Batch Span Ingestion** — Accept multiple spans in a single request for high-throughput pipelines
- **API Key Authentication** — SDK-facing auth using hashed API keys (SHA-256) via a custom Spring Security filter
- **JWT Auth (Access + Refresh)** — Full dual-token JWT flow for human users; three-provider `AuthenticationManager`
- **Role-Based Access Control** — `Role` entity with `RoleName` enum; method-level security via `@EnableMethodSecurity`
- **OpenAPI / Swagger UI** — Auto-generated interactive docs via SpringDoc
- **Paginated Querying** — Fetch traces filtered by userId, sessionId, time range with sorting and pagination
- **H2 Dev Mode** — In-memory database available for local development without PostgreSQL

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.0 |
| Web | Spring MVC (`spring-boot-starter-webmvc`) |
| Security | Spring Security + JWT (`jjwt 0.12.6`) + API Key hash auth |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL (production), H2 (dev/test) |
| API Docs | SpringDoc OpenAPI 3.0 (`springdoc-openapi-starter-webmvc-ui 3.0.0`) |
| JSON | Jackson (`jackson-databind`) |
| Utilities | Lombok |
| Build Tool | Maven (Maven Wrapper included) |
| Dev Tools | Spring Boot DevTools |

---

## Prerequisites

- Java 17+
- Maven 3.8+ (or use the included `./mvnw` wrapper)
- PostgreSQL 13+ (for production use)

---

## Getting Started

### 1. Clone the repository

```bash
git clone https://github.com/DakshRJain737/ai-agent-observatory-backend.git
cd ai-agent-observatory-backend
```

### 2. Configure the database

Create a PostgreSQL database:

```sql
CREATE DATABASE agent;
```

Create `src/main/resources/application.properties` (this file is gitignored — create it manually):

```properties
spring.application.name=agent

spring.datasource.url=jdbc:postgresql://localhost:5432/agent
spring.datasource.username=YOUR_POSTGRES_USERNAME
spring.datasource.password=YOUR_POSTGRES_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> **Tip:** Use `spring.jpa.hibernate.ddl-auto=create` on the first run to auto-create the schema, then switch to `update`.

### 3. Run the application

```bash
./mvnw spring-boot:run
```

Or build and run the JAR:

```bash
./mvnw clean package
java -jar target/agent-0.0.1-SNAPSHOT.jar
```

The server starts on **`http://localhost:8080`**.

---

## API Endpoints

### Auth

| Method | Path | Description |
|---|---|---|
| `POST` | `/user/register` | Register a new user |
| `POST` | `/user/generate-token` | Login and obtain JWT access + refresh tokens |
| `POST` | `/refresh-token` | Get a new access token using refresh token |

### Traces

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/data/traces` | Ingest a new trace |
| `GET` | `/api/traces/{traceId}` | Get details of a specific trace |
| `PUT` | `/api/data/traces/{traceId}/end` | Mark a trace as ended |
| `GET` | `/api/traces` | Query traces by `userId`, `sessionId`, time range, with pagination |

### Spans

| Method | Path | Description |
|---|---|---|
| `POST` | `/api/data/spans` | Ingest a single span |
| `POST` | `/api/data/spans` (batch) | Ingest multiple spans in one request |
| `GET` | `/api/span/{spanId}` | Get a span by ID |

### Users

| Method | Path | Description |
|---|---|---|
| `GET` | `/user/all-users` | List all registered users |

---

## Authentication

The backend uses a **three-provider `AuthenticationManager`**:

| Provider | Used For |
|---|---|
| `DaoAuthenticationProvider` | Username + password login (user registration flow) |
| `JWTAuthenticationProvider` | Validating JWT access tokens on protected routes |
| `ApiKeyHashAuthenticationProvider` | SDK-to-backend auth using SHA-256 hashed API keys |

Requests to the SDK data ingestion endpoints (`/api/data/**`) use the API key flow. Human-facing endpoints use JWT. Public routes (register, login, refresh, Swagger) are permit-all.

---

## API Documentation

Once the server is running:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON:** `http://localhost:8080/v3/api-docs`

---

## Project Structure

```
ai-agent-observatory-backend/
│
├── pom.xml                                          # Maven build config & all dependencies
├── mvnw / mvnw.cmd                                  # Maven wrapper scripts
├── .mvn/wrapper/
│   └── maven-wrapper.properties                     # Maven wrapper version config
│
└── src/
    ├── main/
    │   ├── resources/
    │   │   └── application.properties               # ⚠ Not committed — create manually (see setup)
    │   │
    │   └── java/com/backend_agent_obs/agent/
    │       │
    │       ├── AgentApplication.java                # Spring Boot entry point (@SpringBootApplication)
    │       │
    │       ├── controller/                          # REST controllers (request routing)
    │       │   ├── TraceController.java             # POST /api/data/traces, GET /api/traces, PUT end
    │       │   ├── SpanController.java              # POST /api/data/spans (single & batch), GET /api/span/{id}
    │       │   └── UserController.java              # POST /user/register, GET /user/all-users
    │       │
    │       ├── services/
    │       │   ├── service/                         # Service interfaces (contracts)
    │       │   │   ├── TraceService.java            # startNewTrace, getTraceDetails, endTrace, getTracesBasedOnUser
    │       │   │   ├── SpanService.java             # startNewSpan, startMultipleNewSpan, getSpanById
    │       │   │   └── UserService.java             # findAllUsers, userRegister, loadUserByApiKeyHash
    │       │   └── serviceImpl/                     # Service implementations (business logic)
    │       │       ├── TraceServiceImpl.java        # Trace CRUD + session resolution + paginated queries
    │       │       ├── SpanServiceImpl.java         # Span ingestion + trace/session lookup + batch insert
    │       │       └── UserServiceImpl.java         # User registration, API key hashing, role assignment
    │       │
    │       ├── entities/
    │       │   ├── entity/                          # JPA entities (database tables)
    │       │   │   ├── BaseEntity.java              # Shared auditing fields (createdAt, updatedAt)
    │       │   │   ├── Trace.java                   # @Entity traces — traceId, name, status, metadata, spans[]
    │       │   │   ├── Span.java                    # @Entity spans — spanId, parentSpanId, model, tokens, cost, latency
    │       │   │   ├── Session.java                 # @Entity sessions — sessionId, userId, status, metrics
    │       │   │   ├── User.java                    # @Entity users — username, email, password, apiKeyHash, roles[]
    │       │   │   └── Role.java                    # @Entity roles — roleName (mapped to RoleName enum)
    │       │   └── entityInterface/                 # Domain interfaces (structural contracts)
    │       │       ├── ITrace.java                  # Interface for Trace fields
    │       │       ├── ISpan.java                   # Interface for Span fields
    │       │       └── ISession.java                # Interface for Session fields
    │       │
    │       ├── repo/                                # Spring Data JPA repositories
    │       │   ├── TraceRepository.java             # JPA queries for traces (by traceId, sessionId, time range)
    │       │   ├── SpanRepository.java              # JPA queries for spans (by spanId, traceId)
    │       │   ├── SessionRepository.java           # JPA queries for sessions (by sessionId, userId)
    │       │   ├── UserRepository.java              # JPA queries for users (by username, email, apiKeyHash)
    │       │   └── RoleRepository.java              # JPA queries for roles (by RoleName)
    │       │
    │       ├── dto/                                 # Data Transfer Objects (request/response shapes)
    │       │   ├── entityDto/
    │       │   │   ├── TraceRequestDto.java         # Inbound: traceId, name, sessionId, userId, metadata
    │       │   │   ├── TraceResponseDto.java        # Outbound: traceId, name, sessionId, userId, startTime, endTime
    │       │   │   ├── SpanRequestDto.java          # Inbound: spanId, traceId, parentSpanId, model, tokens, cost, latency...
    │       │   │   ├── SpanResponseDto.java         # Outbound: span data shaped for API response
    │       │   │   └── ErrorDto.java                # Nested error info inside SpanRequestDto
    │       │   └── userDto/
    │       │       ├── UserRegisterDto.java         # Inbound: username, email, password for registration
    │       │       ├── UserDetailsDto.java          # Outbound: safe user info (no password)
    │       │       └── UserLoginRequestDto.java     # Inbound: username + password for login
    │       │
    │       ├── mappers/                             # Object mapping utilities (DTO ↔ Entity)
    │       │   ├── TraceMapperImpl.java             # Trace ↔ TraceRequestDto / TraceResponseDto conversions
    │       │   ├── SpanMapperImpl.java              # Span ↔ SpanRequestDto / SpanResponseDto conversions
    │       │   ├── UserMapperImpl.java              # User ↔ UserRegisterDto / UserDetailsDto conversions
    │       │   └── JsonMapConverter.java            # JPA @Converter — serializes Map<String,Object> to JSON TEXT column
    │       │   ├── ErrorMapperImpl.java             # Error ↔ ErrorRequestDto / ErrorResponseDto 
    │       │
    │       ├── enums/                               # Shared enumerations
    │       │   ├── TraceStatus.java                 # RUNNING, COMPLETED, FAILED, etc.
    │       │   ├── SpanStatus.java                  # RUNNING, COMPLETED, ERROR, etc.
    │       │   ├── SessionStatus.java               # ACTIVE, CLOSED, etc.
    │       │   ├── RoleName.java                    # ROLE_USER, ROLE_ADMIN, etc.
    │       │   └── MetricsInfo.java                 # Aggregate metric labels used in Session
    │       │
    │       ├── auth/                                # Security & authentication layer
    │       │   ├── filter/                          # Spring Security filter chain filters
    │       │   │   ├── JWTAuthenticationFilter.java      # Intercepts login, issues JWT access + refresh tokens
    │       │   │   ├── JWTValidationFilter.java          # Validates JWT access token on incoming requests
    │       │   │   ├── JWTRefreshFilter.java             # Handles /refresh-token, issues new access token
    │       │   │   └── ApiKeyHashAuthenticationFilter.java # Reads API key from header, hashes & authenticates
    │       │   ├── provider/                        # Custom Spring Security AuthenticationProviders
    │       │   │   ├── JWTAuthenticationProvider.java    # Validates JWT token and loads UserDetails
    │       │   │   └── ApiKeyHashAuthenticationProvider.java # Validates API key hash via UserService
    │       │   |── util/
    │       │   |   ├── JwtUtil.java                 # JWT generation & validation (HMAC-SHA, 32-byte key)
    │       │   |   └── ApiKeyHashUtil.java          # SHA-256 hashing utility for API keys
    │       │   |── authentication/
    │       │       ├── JWTAuthenticationToken.java. # JWT authentication token
    │       │       └── ApiKeyHashAuthenticationToken.java # API Key Hash authentication token
    |       |
    │       └── config/
    │           └── SecurityConfig.java              # SecurityFilterChain, AuthenticationManager, PasswordEncoder beans
    │           └── RoleInitializer.java             # Initializes Role for a new user
    │           └── OpenApiConfig.java               # Open Api Configuration applied
    │
    └── test/
        └── java/com/backend_agent_obs/agent/
            └── AgentApplicationTests.java           # Spring Boot context load test
```

---

## Database Schema (Tables)

| Table | Entity | Key Columns |
|---|---|---|
| `users` | `User` | `username`, `email`, `password`, `api_key_hash`, `organization_id` |
| `roles` | `Role` | `role_name` (enum) |
| `sessions` | `Session` | `session_id`, `user_id`, `status`, `start_time` |
| `traces` | `Trace` | `trace_id`, `session_id`, `name`, `status`, `start_time`, `end_time` |
| `spans` | `Span` | `span_id`, `trace_id`, `parent_span_id`, `model`, `input/output_tokens`, `cost`, `latency_ms`, `status` |

All tables inherit `created_at` / `updated_at` from `BaseEntity`.

---

## Related Repositories

| Repo | Description |
|---|---|
| [ai-agent-observatory-sdk](https://github.com/DakshRJain737/ai-agent-observatory-sdk) | Python SDK — decorators for tracing agent workflows with automatic batching, retry logic, and persistent buffering |

---

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add your feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request
