package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.ErrorDto;

public class ErrorMapperImpl {

    public static ErrorDto fromErrorDetailsToErrorDto(String type, String message, String stackTrace) {
        return new ErrorDto(type,message,stackTrace);
    }
}
