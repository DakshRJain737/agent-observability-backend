package com.backend_agent_obs.agent.dto.entityDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SpanResponseDto {
    private String spanId;

    private String traceId;

    private String sessionId;

    private String parentSpanId;

    private String userId;

    private String name;

    private String model;

    private String type;

    private String input;

    private String output;

    private Integer inputTokens;

    private Integer outputTokens;

    private Integer totalTokens;

    private Double cost;

    private Long latencyMs;

    private String spanStatus;

    private ErrorDto error;

    private String version;

    private String environment;

    private Map<String, Object> metadata = new HashMap<>();

    private Instant startTime;

    private Instant endTime;
}
