package com.backend_agent_obs.agent.entities.entityInterface;

import java.time.Instant;
import java.util.Map;

import com.backend_agent_obs.agent.enums.SessionStatus;

public interface ISession {
    String getSessionId();

    String getUserId();

    SessionStatus getStatus();

    Instant getStartTime();

    Instant getEndTime();

    Map<String, Object> getMetadata();
}
