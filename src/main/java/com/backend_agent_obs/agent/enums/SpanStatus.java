package com.backend_agent_obs.agent.enums;

import lombok.Getter;

@Getter
public enum SpanStatus {
    SUCCESS("Operation completed successfully"),
    ERROR("Operation failed"),
    PENDING("Operation in progress"),
    CANCELLED("Operation cancelled");

    private final String description;

    SpanStatus(String description) {
        this.description = description;
    }

}
