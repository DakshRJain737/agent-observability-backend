package com.backend_agent_obs.agent.services.service;

import java.time.LocalDateTime;
import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.TraceRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceResponseDto;
import org.springframework.http.ResponseEntity;

public interface TraceService {

    ResponseEntity<?> startNewTrace(TraceRequestDto traceRequestDto);
    ResponseEntity<?> getTraceDetails(String traceId);
    ResponseEntity<String> endTrace(String traceId);
    ResponseEntity<List<TraceResponseDto>> getTracesBasedOnUser(
            String userId, String sessionId,
            LocalDateTime startTime, LocalDateTime endTime,
            int page, int size,
            String sortBy, String direction);
}
