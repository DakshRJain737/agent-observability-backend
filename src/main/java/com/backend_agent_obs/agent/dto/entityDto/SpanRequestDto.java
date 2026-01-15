package com.backend_agent_obs.agent.dto.entityDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.backend_agent_obs.agent.mappers.JsonMapConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SpanRequestDto {

    @NotBlank(message = "Span ID is required")
    @Size(max = 100)
    private String spanId;

    @NotBlank(message = "Trace ID is required")
    private String traceId;

    @NotBlank(message = "Session ID is required")
    private String sessionId;

    @NotBlank(message = "Parent span ID is required")
    private String parentSpanId;

    @NotBlank(message = "User ID is required")
    private String userId;

    @NotBlank(message = "Span name is required")
    @Size(max = 255)
    private String name;

    @Size(max = 100)
    private String model;

    @NotBlank(message = "Type is required")
    @Size(max = 50)
    private String type;

    @NotBlank(message = "Input is required")
    private String input;

    @NotBlank(message = "Output is required")
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

    @NotNull(message = "Status is required")
    private String spanStatus;

    private ErrorDto error;

    @Size(max = 50)
    private String version;

    @Size(max = 50)
    private String environment;

    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();

    @NotNull(message = "Start time is required")
    private Instant startTime;

    private Instant endTime;
}
