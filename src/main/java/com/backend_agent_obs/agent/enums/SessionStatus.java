package com.backend_agent_obs.agent.enums;

import lombok.Getter;

@Getter
public enum SessionStatus {

    ACTIVE("Active session"),
    ENDED("Session ended"),
    TIMEOUT("Session timed out");

    private final String description;

    SessionStatus(String description) {
        this.description = description;
    }
}
