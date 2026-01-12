package com.backend_agent_obs.agent.entities.entityInterface;

import java.time.Instant;

import com.backend_agent_obs.agent.enums.SpanStatus;

public interface ISpan {
    String getSpanId();

    String getName();

    String getType();

    SpanStatus getStatus();

    Instant getStartTime();

    Instant getEndTime();

    Long getLatencyMs();

    String getUserId();
}
