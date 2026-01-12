package com.backend_agent_obs.agent.dto.entityDto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ErrorDto {

    private String errorType;

    private String errorMessage;

    private String errorStackTrace;

    public ErrorDto(@Size(max = 100) String errorType, String errorMessage, String errorStackTrace) {
    }
}
