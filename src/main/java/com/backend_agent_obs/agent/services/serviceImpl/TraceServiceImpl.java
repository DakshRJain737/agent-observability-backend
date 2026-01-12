package com.backend_agent_obs.agent.services.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.backend_agent_obs.agent.dto.entityDto.TraceDto;
import com.backend_agent_obs.agent.entities.entity.Trace;
import com.backend_agent_obs.agent.mappers.TraceMapperImpl;
import com.backend_agent_obs.agent.repo.SessionRespository;
import com.backend_agent_obs.agent.repo.TraceRepository;
import com.backend_agent_obs.agent.services.service.TraceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TraceServiceImpl implements TraceService {

    final private TraceRepository traceRepository;
    final private SessionRespository sessionRespository;
    final private TraceMapperImpl traceMapper;

    public TraceServiceImpl(TraceRepository traceRepository,
                            SessionRespository sessionRespository,
                            TraceMapperImpl traceMapper) {
        this.traceRepository = traceRepository;
        this.sessionRespository = sessionRespository;
        this.traceMapper = traceMapper;
    }

    @Override
    @Transactional
    public ResponseEntity<?> startNewTrace(TraceDto traceDto) {
//        Optional<Session> optionalSession =
//                sessionRespository.findBySessionId(traceDto.getSessionId());
//        if(optionalSession.isEmpty()) {
//            log.info("No session id as{}", traceDto.getSessionId());
//            Session session = new Session();
//            session.setSessionId(traceDto.getSessionId());
//            session.setUserId(traceDto.getUserId());
//            session.setTimestamp(LocalDateTime.now());
//
//            Trace trace = traceMapper.fromTraceDtoToTrace(traceDto);
//            trace.setTraceStatus(TraceStatus.ACTIVE);
//            // Keep for now will change to better impl
//            trace.setEndTime(null);
//
//            sessionRespository.save(session);
//            traceRepository.save(trace);
//
//            log.info("Session{} and Trace{} created and stored",
//                    session.getSessionId(),
//                    trace.getTraceId());

            return ResponseEntity.ok("Session and Trace created and stored");
//        }
//        Trace trace = traceMapper.fromTraceDtoToTrace(traceDto);
//        trace.setTraceStatus(TraceStatus.ACTIVE);
//        trace.setEndTime(null);
//        traceRepository.save(trace);
//        log.info("Trace{} created and stored",
//                    trace.getTraceId());

//        return ResponseEntity.ok("Trace created and stored");
    }

    @Override
    public ResponseEntity<?> getTraceDetails(String traceId) {
        Optional<Trace> trace = traceRepository.findByTraceId(traceId);
        if(trace.isEmpty()) {
            return ResponseEntity.ok("No Trace found with trace id: " + traceId);
        }
        return ResponseEntity.ok(trace.get());
    }

    @Override
    public ResponseEntity<String> endTrace(String traceId) {
//        Optional<Trace> opTrace = traceRepository.findByTraceId(traceId);
//        if (opTrace.isEmpty()) {
//            return ResponseEntity.ok("No trace found with trace id: " + traceId);
//        }
//
//        Trace trace = opTrace.get();
//        trace.setEndTime(LocalDateTime.now());
//        traceRepository.save(trace);
        return ResponseEntity.ok("Trace ended successfully");
    }

    @Override
    public ResponseEntity<List<TraceDto>> getTracesBasedOnUser(
            String userId,
            String sessionId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int page,
            int size,
            String sortBy,
            String direction) {

//        Sort sort = direction.equalsIgnoreCase("desc")
//                ? Sort.by(sortBy).descending()
//                : Sort.by(sortBy).ascending();
//
//        Pageable pageable = PageRequest.of(page, size, sort);
//
//        List<TraceDto> traces = traceRepository
//                .findTracesBySessionAndUserAndTimeRange(
//                        sessionId, userId, startTime, endTime, pageable
//                )
//                .stream()
//                .map(traceMapper::fromTraceToTraceDto)
//                .toList();

//        return ResponseEntity.ok(traces);
        return ResponseEntity.ok(null);
    }


}
