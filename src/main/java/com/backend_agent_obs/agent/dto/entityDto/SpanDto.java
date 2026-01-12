package com.backend_agent_obs.agent.dto.entityDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.backend_agent_obs.agent.enums.SpanStatus;
import com.backend_agent_obs.agent.mappers.JsonMapConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SpanDto {

    @NotBlank(message = "Span ID is required")
    @Size(max = 100)
    private String spanId;

    private String parentSpanId;

    @NotBlank(message = "Trace ID is required")
    @Size(max = 100)
    private String traceId;

    @NotBlank(message = "Session ID is required")
    @Size(max = 100)
    private String sessionId;

    @NotBlank(message = "Span name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String model;

    @NotBlank(message = "Type is required")
    @Size(max = 50)
    private String type;

    private String input;

    private String output;

    @Min(value = 0, message = "Input tokens cannot be negative")
    private Integer inputTokens;

    @Min(value = 0, message = "Output tokens cannot be negative")
    private Integer outputTokens;

    @Min(value = 0, message = "Total tokens cannot be negative")
    private Integer totalTokens;

    @DecimalMin(value = "0.0", message = "Cost cannot be negative")
    private Double cost;

    @Min(value = 0, message = "Latency cannot be negative")
    private Long latencyMs;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    private Instant endTime;

    private ErrorDto error;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private SpanStatus status = SpanStatus.SUCCESS;

    @Size(max = 50)
    private String version;

    private String environment;

    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();
}
