package com.backend_agent_obs.agent.entities.entityInterface;

import java.time.Instant;
import java.util.Map;

import com.backend_agent_obs.agent.enums.TraceStatus;

public interface ITrace {
    String getTraceId();

    String getName();

    TraceStatus getStatus();

    Instant getStartTime();

    Instant getEndTime();

    Map<String, Object> getMetadata();

    String getUserId();
}
