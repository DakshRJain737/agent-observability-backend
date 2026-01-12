package com.backend_agent_obs.agent.enums;

import lombok.Getter;

@Getter
public enum MetricsInfo {

    LATENCY("Execution latency in milliseconds"),
    COST("Cost in USD"),
    TOKENS("Total tokens used"),
    INPUT_TOKENS("Input tokens used"),
    ERROR_RATE("Error rate percentage"),
    THROUGHPUT("Requests per second");

    private final String description;

    MetricsInfo(String description) {
        this.description = description;
    }

}
