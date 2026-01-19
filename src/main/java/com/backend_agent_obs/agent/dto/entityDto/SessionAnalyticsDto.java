package com.backend_agent_obs.agent.dto.entityDto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class SessionAnalyticsDto {
    private Map<String, TraceAnalyticsDto> traceMap;

    public SessionAnalyticsDto() {
        this.traceMap = new HashMap<>();
    }


}
