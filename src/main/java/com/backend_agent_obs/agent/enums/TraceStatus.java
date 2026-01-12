package com.backend_agent_obs.agent.enums;

import lombok.Getter;

@Getter
public enum TraceStatus {

    ACTIVE("Trace in progress"),
    ENDED("Trace completed successfully"),
    FAILED("Trace failed");

    private final String description;

    TraceStatus(String description) {
        this.description = description;
    }

}
