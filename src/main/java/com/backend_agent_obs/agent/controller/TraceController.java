package com.backend_agent_obs.agent.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.TraceDto;
import com.backend_agent_obs.agent.mappers.TraceMapperImpl;
import com.backend_agent_obs.agent.services.service.TraceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TraceController {

    final private TraceMapperImpl traceMapper;
    final private TraceService traceService;

    public TraceController(TraceMapperImpl traceMapper, TraceService traceService) {
        this.traceMapper = traceMapper;
        this.traceService = traceService;
    }

    @PostMapping("/traces")
    public ResponseEntity<?> startNewTrace(@RequestBody TraceDto traceDto) {
        return traceService.startNewTrace(traceDto);
    }

    @GetMapping("/traces/{traceId}")
    public ResponseEntity<?> getTraceDetails(@PathVariable String traceId) {
        return traceService.getTraceDetails(traceId);
    }

    @PutMapping("/traces/{traceId}/end")
    public ResponseEntity<String> endTrace(@PathVariable String traceId) {
        return traceService.endTrace(traceId);
    }

    @GetMapping("/traces")
    public ResponseEntity<List<TraceDto>> getTracesBasedOnUser(
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
