package com.backend_agent_obs.agent.controller;

import java.util.List;

import com.backend_agent_obs.agent.dto.entityDto.SpanRequestDto;
import com.backend_agent_obs.agent.services.service.SpanService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SpanController {

    private SpanService spanService;

    @PostMapping("/data/spans")
    public ResponseEntity<?> startNewSpan(@RequestBody SpanRequestDto spanRequestDto) {
        return spanService.startNewSpan(spanRequestDto);
    }

    @PostMapping("/data/spans")
    public ResponseEntity<?> startMultipleNewSpan(@RequestBody List<SpanRequestDto> spans) {
        return spanService.startMultipleNewSpan(spans);
    }

    @GetMapping("/span/{spanId}")
    public ResponseEntity<?> getSpanById(@PathVariable("spanId") String spanId) {
        return spanService.getSpanById(spanId);
    }

}
