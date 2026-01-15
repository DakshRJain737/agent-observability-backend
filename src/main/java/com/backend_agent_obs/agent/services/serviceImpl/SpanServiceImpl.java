package com.backend_agent_obs.agent.services.serviceImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.backend_agent_obs.agent.dto.entityDto.SpanRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.SpanResponseDto;
import com.backend_agent_obs.agent.entities.entity.Session;
import com.backend_agent_obs.agent.entities.entity.Span;
import com.backend_agent_obs.agent.entities.entity.Trace;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.mappers.SpanMapperImpl;
import com.backend_agent_obs.agent.repo.SessionRepository;
import com.backend_agent_obs.agent.repo.SpanRepository;
import com.backend_agent_obs.agent.repo.TraceRepository;
import com.backend_agent_obs.agent.services.service.SpanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SpanServiceImpl implements SpanService {

    SpanRepository spanRepository;
    TraceRepository traceRepository;
    SessionRepository sessionRepository;

    @Transactional
    @Override
    public ResponseEntity<?> startNewSpan(SpanRequestDto spanRequestDto) {
        if (spanRequestDto == null || spanRequestDto.getSpanId() == null) {
            return ResponseEntity.badRequest().body("Invalid span request");
        }

        Trace trace = traceRepository.findByTraceId(spanRequestDto.getTraceId()).orElseGet(() -> {
            Trace newTrace = SpanMapperImpl.fromSpanRequestDtoToTrace(spanRequestDto);
            if (newTrace.getName() == null || newTrace.getName().isEmpty()) {
                newTrace.setName("Default Trace");
            }
            return newTrace;
        });

        Session session = sessionRepository.findBySessionId(spanRequestDto.getSessionId()).orElseGet(() -> {
            Session newSession = new Session();
            newSession.setSessionId(spanRequestDto.getSessionId());
            newSession.setUserId(spanRequestDto.getUserId());

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal() instanceof User) {
                newSession.setCustomer((User) auth.getPrincipal());
            }
            return newSession;
        });

        if (trace.getSession() == null) {
            session.addTrace(trace);
        }

        Span span = SpanMapperImpl.fromSpanRequestDtoToSpan(spanRequestDto);

        if (spanRequestDto.getParentSpanId() != null) {
            spanRepository.findBySpanId(spanRequestDto.getParentSpanId()).ifPresent(parentSpan -> {
                parentSpan.addChildSpan(span);
            });
        }

        trace.addSpan(span);

        try {
            sessionRepository.save(session);

            return ResponseEntity.ok(Map.of("message", "Session, Trace and Span saved successfully", "spanId", span.getSpanId(), "traceId", trace.getTraceId(), "sessionId", session.getSessionId()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error saving entities: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> startMultipleNewSpan(List<SpanRequestDto> spanRequestDtoList) {
        for(SpanRequestDto spanRequestDto : spanRequestDtoList) {
            ResponseEntity<?> responseEntity = startNewSpan(spanRequestDto);
            if(responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
                return responseEntity;
            }
        }
        return ResponseEntity.ok("All the spans successfully saved");
    }

    @Override
    public ResponseEntity<?> getSpanById(String spanId) {
        Optional<Span> optionalSpan = spanRepository.findBySpanId(spanId);
        if(optionalSpan.isEmpty()) {
            return ResponseEntity.badRequest().body("No such span present with spanId : " + spanId);
        }
        SpanResponseDto spanResponseDto = SpanMapperImpl.fromSpanToSpanResponseDto(optionalSpan.get());
        return ResponseEntity.ok(spanResponseDto);
    }
}
