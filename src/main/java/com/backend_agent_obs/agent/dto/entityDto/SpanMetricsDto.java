package com.backend_agent_obs.agent.dto.entityDto;

import lombok.Data;

@Data
public class SpanMetricsDto {
    private long latencyMs;
    private int tokensUsed;
    private double cost;
    private String modelUsed;
}
