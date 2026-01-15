package com.backend_agent_obs.agent.services.service;

import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.SpanRequestDto;
import org.springframework.http.ResponseEntity;

public interface SpanService {
    ResponseEntity<?> startNewSpan(SpanRequestDto spanRequestDto);

    ResponseEntity<?> startMultipleNewSpan(List<SpanRequestDto> spanRequestDtoList);

    ResponseEntity<?> getSpanById(String spanId);
}
