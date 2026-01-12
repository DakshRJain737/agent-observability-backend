package com.backend_agent_obs.agent.entities.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backend_agent_obs.agent.entities.entityInterface.ISession;
import com.backend_agent_obs.agent.enums.MetricsInfo;
import com.backend_agent_obs.agent.enums.SessionStatus;
import com.backend_agent_obs.agent.mappers.JsonMapConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sessions", indexes = {@Index(name = "idx_session_user", columnList = "user_id"), @Index(name = "idx_session_status", columnList = "status"), @Index(name = "idx_session_start_time", columnList = "start_time")})
public class Session extends BaseEntity implements ISession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String sessionId;

    // End-user ID (the person using the chatbot/agent being monitored)
    // NOT the SystemUser who owns this observability account
    @NotBlank(message = "User ID is required")
    @Size(max = 100)
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SessionStatus status = SessionStatus.ACTIVE;

    // "latency", "cost", "tokens"
    // correct this
    @Enumerated(EnumType.STRING)
    private MetricsInfo type;
    private Double value;
    // when the metrics was calculated

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> metadata = new HashMap<>();

    @Convert(converter = JsonMapConverter.class)
    @Column(name = "user_metadata", columnDefinition = "TEXT")
    private Map<String, Object> userMetadata = new HashMap<>();

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Trace> traces = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "system_user_id", nullable = false)
    private User customer;

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (startTime == null) {
            startTime = Instant.now();
        }
        if (status == null) {
            status = SessionStatus.ACTIVE;
        }
    }

    @PreUpdate
    private void validate() {
        if (endTime != null && startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalStateException("End time cannot be before start time");
        }
        if (status == SessionStatus.ENDED && endTime == null) {
            throw new IllegalStateException("Ended session must have end time");
        }
    }

    public void endSession() {
        if (this.status == SessionStatus.ENDED) {
            throw new IllegalStateException("Session already ended");
        }
        this.status = SessionStatus.ENDED;
        this.endTime = Instant.now();
    }

    public void addTrace(Trace trace) {
        traces.add(trace);
        trace.setSession(this);
    }

    public void removeTrace(Trace trace) {
        traces.remove(trace);
        trace.setSession(null);
    }
}
