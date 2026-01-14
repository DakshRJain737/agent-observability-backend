package com.backend_agent_obs.agent.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.TraceRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceResponseDto;
import com.backend_agent_obs.agent.mappers.TraceMapperImpl;
import com.backend_agent_obs.agent.services.service.TraceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TraceController {

    final private TraceService traceService;

    public TraceController(TraceService traceService) {
        this.traceService = traceService;
    }

    @PostMapping("/data/traces")
    public ResponseEntity<?> startNewTrace(@RequestBody TraceRequestDto traceRequestDto) {
        return traceService.startNewTrace(traceRequestDto);
    }

//    @PreAuthorize(value = "hasRole='USER'")
    @GetMapping("/traces/{traceId}")
    public ResponseEntity<?> getTraceDetails(@PathVariable String traceId) {
        return traceService.getTraceDetails(traceId);
    }

    @PutMapping("/traces/data/{traceId}/end")
    public ResponseEntity<String> endTrace(@PathVariable String traceId) {
        return traceService.endTrace(traceId);
    }

    @GetMapping("/traces")
    public ResponseEntity<List<TraceResponseDto>> getTracesParticularToUser(
            @RequestParam String userId,
            @RequestParam String sessionId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        if(startTime.isAfter(endTime)) {
            return ResponseEntity.badRequest().build();
        }
        return traceService.getTracesBasedOnUser(
                userId, sessionId, startTime, endTime, page, size, sortBy, direction
        );

    }
}
