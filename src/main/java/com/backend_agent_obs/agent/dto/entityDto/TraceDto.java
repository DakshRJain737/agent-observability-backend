package com.backend_agent_obs.agent.dto.entityDto;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.backend_agent_obs.agent.mappers.JsonMapConverter;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TraceDto {

    @NotBlank(message = "Trace ID is required")
    @Size(max = 100)
    private String traceId;
    // name of the trace (Customer Support ChatBot)
    @NotBlank(message = "Trace name is required")
    @Size(max = 255)
    private String name;

    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> metadata = new HashMap<>();

    @NotBlank(message = "Session Id required")
    private String sessionId;

    @NotBlank(message = "User ID is required")
    @Size(max = 100)
    private String userId;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    private Instant endTime;
}
