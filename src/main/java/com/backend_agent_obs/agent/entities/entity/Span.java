package com.backend_agent_obs.agent.entities.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.backend_agent_obs.agent.entities.entityInterface.ISpan;
import com.backend_agent_obs.agent.enums.SpanStatus;
import com.backend_agent_obs.agent.mappers.JsonMapConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "spans", indexes = {@Index(name = "idx_span_trace", columnList = "trace_id"), @Index(name = "idx_span_parent", columnList = "parent_span_id"), @Index(name = "idx_span_status", columnList = "status"), @Index(name = "idx_span_start_time", columnList = "start_time"), @Index(name = "idx_span_type", columnList = "type"), @Index(name = "idx_span_model", columnList = "model")})
public class Span extends BaseEntity implements ISpan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Span ID is required")
    @Size(max = 100)
    @Column(name = "span_id", unique = true, nullable = false, length = 100)
    private String spanId;

    @NotBlank(message = "Span name is required")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @Size(max = 100)
    @Column(length = 100)
    private String model;

    @NotBlank(message = "Type is required")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String type;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String output;

    @Min(value = 0, message = "Input tokens cannot be negative")
    @Column(name = "input_tokens")
    private Integer inputTokens;

    @Min(value = 0, message = "Output tokens cannot be negative")
    @Column(name = "output_tokens")
    private Integer outputTokens;

    @Min(value = 0, message = "Total tokens cannot be negative")
    @Column(name = "total_tokens")
    private Integer totalTokens;

    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    @Column
    private Double cost;

    @Min(value = 0, message = "Latency cannot be negative")
    @Column(name = "latency_ms")
    private Long latencyMs;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpanStatus status = SpanStatus.SUCCESS;

    @Size(max = 100)
    @Column(name = "error_type", length = 100)
    private String errorType;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "error_stack_trace", columnDefinition = "TEXT")
    private String errorStackTrace;

    @Size(max = 50)
    @Column(length = 50)
    private String version;

    @Size(max = 50)
    @Column(length = 50)
    private String environment;

    @Convert(converter = JsonMapConverter.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> metadata = new HashMap<>();

    @NotNull(message = "Start time is required")
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @NotNull(message = "Trace is required")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trace_id", nullable = false)
    @ToString.Exclude
    private Trace trace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_span_id")
    @ToString.Exclude
    private Span parentSpan;

    @OneToMany(mappedBy = "parentSpan", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Span> childSpans = new ArrayList<>();

    @PrePersist
    @Override
    protected void onCreate() {
        super.onCreate();
        if (startTime == null) {
            startTime = Instant.now();
        }
        if (status == null) {
            status = SpanStatus.SUCCESS;
        }
    }

    @PreUpdate
    private void validate() {
        if (endTime != null && startTime != null && endTime.isBefore(startTime)) {
            throw new IllegalStateException("End time cannot be before start time");
        }
        if (totalTokens != null && inputTokens != null && outputTokens != null) {
            int expectedTotal = inputTokens + outputTokens;
            if (!totalTokens.equals(expectedTotal)) {
                throw new IllegalStateException(
                        String.format("Total tokens (%d) must equal input (%d) + output (%d)",
                                totalTokens, inputTokens, outputTokens)
                );
            }
        }
        if (status == SpanStatus.ERROR && (errorType == null || errorMessage == null)) {
            throw new IllegalStateException("Error spans must have error type and message");
        }
    }

    public String getEndUserId() {
        return trace != null && trace.getSession() != null ? trace.getSession().getUserId() : null;
    }

    public void completeSpan() {
        if (this.endTime != null) {
            throw new IllegalStateException("Span already completed");
        }
        if (this.status == SpanStatus.ERROR) {
            return; // Don't override error status
        }
        this.status = SpanStatus.SUCCESS;
        this.endTime = Instant.now();
        if (this.startTime != null) {
            this.latencyMs = endTime.toEpochMilli() - startTime.toEpochMilli();
        }
    }

    public void markError(String errorType, String errorMessage, String stackTrace) {
        this.status = SpanStatus.ERROR;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.errorStackTrace = stackTrace;
        this.endTime = Instant.now();
        if (this.startTime != null) {
            this.latencyMs = endTime.toEpochMilli() - startTime.toEpochMilli();
        }
    }

    public void addChildSpan(Span child) {
        if (child == null) {
            throw new IllegalArgumentException("Child span cannot be null");
        }
        if (!childSpans.contains(child)) {
            childSpans.add(child);
            child.setParentSpan(this);
        }
    }

    public void removeChildSpan(Span child) {
        if (child != null && childSpans.remove(child)) {
            child.setParentSpan(null);
        }
    }

    @Override
    public String getUserId() {
        return trace != null && trace.getSession() != null
                ? trace.getSession().getUserId()
                : null;
    }

    public boolean isCompleted() {
        return this.endTime != null;
    }

    public boolean hasError() {
        return this.status == SpanStatus.ERROR;
    }
}
