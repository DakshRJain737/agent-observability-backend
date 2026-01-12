package com.backend_agent_obs.agent.services.service;

import java.time.LocalDateTime;
import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.TraceDto;
import org.springframework.http.ResponseEntity;

public interface TraceService {

    ResponseEntity<?> startNewTrace(TraceDto traceDto);
    ResponseEntity<?> getTraceDetails(String traceId);
    ResponseEntity<String> endTrace(String traceId);
    ResponseEntity<List<TraceDto>> getTracesBasedOnUser(
            String userId, String sessionId,
            LocalDateTime startTime, LocalDateTime endTime,
            int page, int size,
            String sortBy, String direction);
}
