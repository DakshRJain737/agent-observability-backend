package com.backend_agent_obs.agent.services.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.backend_agent_obs.agent.dto.entityDto.TraceRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceResponseDto;
import com.backend_agent_obs.agent.entities.entity.Session;
import com.backend_agent_obs.agent.entities.entity.Trace;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.mappers.TraceMapperImpl;
import com.backend_agent_obs.agent.repo.SessionRepository;
import com.backend_agent_obs.agent.repo.TraceRepository;
import com.backend_agent_obs.agent.services.service.TraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TraceServiceImpl implements TraceService {

    final private TraceRepository traceRepository;
    final private SessionRepository sessionRepository;

    public TraceServiceImpl(TraceRepository traceRepository, SessionRepository sessionRepository) {
        this.traceRepository = traceRepository;
        this.sessionRepository = sessionRepository;

    }

    @Override
    @Transactional
    public ResponseEntity<?> startNewTrace(TraceRequestDto traceRequestDto) {
        Optional<Session> optionalSession = sessionRepository.findBySessionId(traceRequestDto.getSessionId());
        Trace trace = TraceMapperImpl.fromTraceRequestDtoToTrace(traceRequestDto);

        if (optionalSession.isEmpty()) {
            log.info("No session id as{}", traceRequestDto.getSessionId());
            Session session = TraceMapperImpl.fromTraceRequestDtoToSession(traceRequestDto);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            assert auth != null;
            User user = (User) auth.getPrincipal();
            session.setCustomer(user);
            session.addTrace(trace);
            trace.setSession(session);
            sessionRepository.save(session);
            traceRepository.save(trace);

            log.info("Session{} and Trace{} created and stored", session.getSessionId(), trace.getTraceId());

            return ResponseEntity.ok("Session and Trace created and stored");
        }
        trace.setSession(optionalSession.get());
        traceRepository.save(trace);
        log.info("Trace with traceId {} created and stored(Session already exist)", trace.getTraceId());

        return ResponseEntity.ok("Trace created and stored(Session already exist)");
    }

    @Override
    public ResponseEntity<?> getTraceDetails(String traceId) {
        Optional<Trace> trace = traceRepository.findByTraceId(traceId);
        if (trace.isEmpty()) {
            return ResponseEntity.ok("No Trace found with trace id: " + traceId);
        }
        TraceResponseDto traceResponseDto = TraceMapperImpl.fromTraceToTraceResponseDto(trace.get());
        return ResponseEntity.ok(traceResponseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<String> endTrace(String traceId) {
        Optional<Trace> opTrace = traceRepository.findByTraceId(traceId);
        if (opTrace.isEmpty()) {
            return ResponseEntity.ok("No trace found with trace id: " + traceId);
        }
        Trace trace = opTrace.get();
        trace.endTrace();
        traceRepository.save(trace);
        return ResponseEntity.ok("Trace ended successfully");
    }

    @Override
    public ResponseEntity<List<TraceResponseDto>> getTracesBasedOnUser(String userId, String sessionId, LocalDateTime startTime, LocalDateTime endTime, int page, int size, String sortBy, String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        List<TraceResponseDto> traces = traceRepository
                .findTracesBySessionAndUserAndTimeRange(
                        sessionId, userId, startTime, endTime, pageable
                )
                .stream()
                .map(TraceMapperImpl::fromTraceToTraceResponseDto)
                .toList();

        return ResponseEntity.ok(traces);
    }
}
