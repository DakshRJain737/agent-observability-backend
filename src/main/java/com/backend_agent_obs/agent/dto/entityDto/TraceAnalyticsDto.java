package com.backend_agent_obs.agent.dto.entityDto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TraceAnalyticsDto {
    // string is span id
    private Map<String, SpanMetricsDto> spanMap;

    private long totalLatency;
    private int totalTokens;
    private double totalCost;

    public TraceAnalyticsDto() {
        this.spanMap = new HashMap<>();
    }
}
