package com.backend_agent_obs.agent.entities.entity;

import java.util.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", indexes = {@Index(name = "idx_user_email", columnList = "email"), @Index(name = "idx_user_username", columnList = "username"), @Index(name = "idx_user_api_key", columnList = "api_key_hash"), @Index(name = "idx_user_org", columnList = "organization_id")})
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 100 characters")
    @Column(unique = true, nullable = false, length = 100)
    private String username;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "api_key_hash", unique = true, length = 255)
    private String apiKeyHash;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Session> sessions = new ArrayList<>();

    @NotBlank(message = "Organization ID is required")
    @Column(name = "organization_id", nullable = false, length = 100)
    private String organizationId;

    @Column(name = "account_locked", nullable = false)
    private boolean accountLocked = false;

    @Column(name = "account_expired", nullable = false)
    private boolean accountExpired = false;

    @Column(name = "credentials_expired", nullable = false)
    private boolean credentialsExpired = false;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();

    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }

    public void lockAccount() {
        this.accountLocked = true;
    }

    public void unlockAccount() {
        this.accountLocked = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }
}

// ## **1. Trace APIs (Top-level conversation/session)**

// | Endpoint                    | Method | Description                | Request Body / Params                                                         |
// | --------------------------- | ------ | -------------------------- | ----------------------------------------------------------------------------- |
// | `/api/traces`               | POST   | Start a new trace          | `{ "name": "...", "sessionId": "...", "userId": "...", "metadata": {...} }` |
// | `/api/traces/:traceId`     | GET    | Get details of a trace     | Path param `traceId`                                                         |
// | `/api/traces`               | GET    | List traces (with filters) | Query params: `userId`, `sessionId`, `date_from`, `date_to`                 |
// | `/api/traces/:traceId/end` | POST   | End a trace                | `{}` (just marks trace ended)                                                 |

// ---

// ## **2. Span APIs (Individual operations inside a trace)**

// | Endpoint              | Method | Description                        | Request Body / Params                                                                                                                                         |
// | --------------------- | ------ | ---------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------- |
// | `/api/spans`          | POST   | Add a new span                     | `{ "traceId": "...", "parentSpanId": "...", "name": "...", "type": "...", "input": "...", "output": "...", "tokens": {...}, "cost": ..., "latency": ... }` |
// | `/api/spans/batch`    | POST   | Add multiple spans at once (batch) | `{ "spans": [ {...}, {...} ] }`                                                                                                                               |
// | `/api/spans/:span_id` | GET    | Get details of a span              | Path param `span_id`                                                                                                                                          |
// | `/api/spans`          | GET    | List spans (filters)               | Query params: `traceId`, `sessionId`, `userId`, `status`, `date_from`, `date_to`                                                                           |

// ---

// ## **3. Session APIs (Optional, if you want session-level grouping)**

// | Endpoint                    | Method | Description                          | Request Body / Params                           |
// | --------------------------- | ------ | ------------------------------------ | ----------------------------------------------- |
// | `/api/sessions/:sessionId` | GET    | Get session details & related traces | Path param `sessionId`                         |
// | `/api/sessions`             | GET    | List sessions                        | Query params: `userId`, `date_from`, `date_to` |

// ---

// ## **4. Analytics / Dashboard APIs**

// | Endpoint               | Method | Description                       | Request Body / Params                                                  |
// | ---------------------- | ------ | --------------------------------- | ---------------------------------------------------------------------- |
// | `/api/metrics/costs`   | GET    | Total cost per user/session/model | Query params: `userId`, `sessionId`, `model`, `date_from`, `date_to` |
// | `/api/metrics/latency` | GET    | Average latency per span/type     | Query params similar to above                                          |
// | `/api/metrics/tokens`  | GET    | Token usage stats                 | Query params similar to above                                          |
// | `/api/errors`          | GET    | List failed spans with errors     | Query params: `status=error`, `userId`, `traceId`, `date_from`       |

// ---

// ## **5. Admin / Maintenance APIs (Optional)**

// | Endpoint                     | Method | Description                            |
// | ---------------------------- | ------ | -------------------------------------- |
// | `/api/persisted-spans/clear` | POST   | Clear old persisted spans from disk/db |
// | `/api/health`                | GET    | Simple health check                    |


// *   ** **** * * ** * ** * * * * * * * ** * ** * * * * ** * ** * * * * *** * ** *** * *

// | Field        | Type          | Description                                |
// | ------------ | ------------- | ------------------------------------------ |
// | `id`         | UUID / string | Unique trace ID                            |
// | `name`       | string        | Trace name (e.g., "Customer Support Chat") |
// | `sessionId` | UUID / string | Associated session ID                      |
// | `userId`    | UUID / string | User who initiated the trace               |
// | `metadata`   | JSON / dict   | Custom metadata (channel, country, etc.)   |
// | `status`     | string        | "active" / "ended"                         |
// | `startTime` | datetime      | When trace started                         |
// | `endTime`   | datetime      | When trace ended                           |
// | `spans`      | List[Span]    | One-to-many relationship with spans        |


// | Field            | Type          | Description                                  |
// | ---------------- | ------------- | -------------------------------------------- |
// | `id`             | UUID / string | Unique span ID                               |
// | `traceId`       | UUID / string | Foreign key to Trace                         |
// | `parentSpanId` | UUID / string | Optional, for nested spans                   |
// | `sessionId`     | UUID / string | Optional, duplicate for quick queries        |
// | `userId`        | UUID / string | Optional, duplicate for quick queries        |
// | `name`           | string        | Name of operation/function                   |
// | `type`           | string        | e.g., "llm", "llm_streaming", "api_call"     |
// | `model`          | string        | Model used (e.g., "gpt-4o-mini")             |
// | `input`          | text          | Input data to the operation                  |
// | `output`         | text          | Output data/result                           |
// | `inputTokens`   | int           | Input tokens counted                         |
// | `outputTokens`  | int           | Output tokens counted                        |
// | `totalTokens`   | int           | Input + Output tokens                        |
// | `cost`           | float         | Calculated cost for this operation           |
// | `latency_ms`     | int           | Execution time in milliseconds               |
// | `status`         | string        | "success" / "error"                          |
// | `error`          | JSON / dict   | Error info if failed                         |
// | `tags`           | List[str]     | Optional tags for filtering                  |
// | `version`        | string        | Optional version info (e.g., prompt version) |
// | `metadata`       | JSON / dict   | Custom metadata                              |
// | `startTime`     | datetime      | Span start timestamp                         |
// | `endTime`       | datetime      | Span end timestamp                           |


// | Field        | Type          | Description              |
// | ------------ | ------------- | ------------------------ |
// | `id`         | UUID / string | Session ID               |
// | `userId`    | UUID / string | User who owns session    |
// | `status`     | string        | "active" / "ended"       |
// | `startTime` | datetime      | Session start time       |
// | `endTime`   | datetime      | Session end time         |
// | `metadata`   | JSON / dict   | Optional metadata        |
// | `traces`     | List[Trace]   | One-to-many relationship |

// | Field       | Type      | Description                 |
// | ----------- | --------- | --------------------------- |
// | `id`        | UUID      | Unique metric record ID     |
// | `type`      | string    | "latency", "cost", "tokens" |
// | `value`     | float/int | Metric value                |
// | `traceId`  | UUID      | Optional reference          |
// | `span_id`   | UUID      | Optional reference          |
// | `userId`   | UUID      | Optional reference          |
// | `timestamp` | datetime  | When metric was calculated  |


// Relationships

// Trace → Span: One-to-many

// Session → Trace: One-to-many (optional)

// Span → Span: Nested spans using parentSpanId