package com.backend_agent_obs.agent.entities.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backend_agent_obs.agent.entities.entityInterface.ITrace;
import com.backend_agent_obs.agent.enums.TraceStatus;
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
@Table(name = "traces", indexes = {@Index(name = "idx_trace_session", columnList = "session_id"), @Index(name = "idx_trace_status", columnList = "status"), @Index(name = "idx_trace_start_time", columnList = "start_time"),})
public class Trace extends BaseEntity implements ITrace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Trace ID is required")
    @Size(max = 100)
    @Column(name = "trace_id", unique = true, nullable = false, length = 100)
    private String traceId;

    @NotBlank(message = "Trace name is required")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> metadata = new HashMap<>();

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TraceStatus status = TraceStatus.ACTIVE;

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @NotNull(message = "Session is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    @ToString.Exclude
    private Session session;

    @OneToMany(mappedBy = "trace", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Span> spans = new ArrayList<>();

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (startTime == null) {
            startTime = Instant.now();
        }
        if (status == null) {
            status = TraceStatus.ACTIVE;
        }
    }

    @PreUpdate
    private void validate() {
        if (endTime != null && startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalStateException("End time cannot be before start time");
        }
        if (status == TraceStatus.ENDED && endTime == null) {
            throw new IllegalStateException("Ended trace must have end time");
        }
    }

    public String getEndUserId() {
        return session != null ? session.getUserId() : null;
    }

    public void endTrace() {
        if (this.status == TraceStatus.ENDED) {
            throw new IllegalStateException("Trace already ended");
        }
        this.status = TraceStatus.ENDED;
        this.endTime = Instant.now();
    }

    public void failTrace(String reason) {
        this.status = TraceStatus.FAILED;
        this.endTime = Instant.now();
        this.metadata.put("failure_reason", reason);
    }

    public void addSpan(Span span) {
        spans.add(span);
        span.setTrace(this);
    }

    public void removeSpan(Span span) {
        spans.remove(span);
        span.setTrace(null);
    }

    @Override
    public String getUserId() {
        return session.getUserId();
    }
}
