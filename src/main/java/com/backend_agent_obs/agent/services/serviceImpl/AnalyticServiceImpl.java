package com.backend_agent_obs.agent.services.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.backend_agent_obs.agent.dto.entityDto.SessionAnalyticsDto;
import com.backend_agent_obs.agent.dto.entityDto.SpanMetricsDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceAnalyticsDto;
import com.backend_agent_obs.agent.entities.entity.Session;
import com.backend_agent_obs.agent.entities.entity.Span;
import com.backend_agent_obs.agent.entities.entity.Trace;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.repo.UserRepository;
import com.backend_agent_obs.agent.services.service.AnalyticsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AnalyticServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;

    public AnalyticServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> getAllMetrics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDetails userDetails = (User) authentication.getPrincipal();
        Optional<User> optionalUser = userRepository.findByUsername(userDetails.getUsername());
        if(optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("No user found");
        }
        User user = optionalUser.get();
        List<Session> sessionList = user.getSessions();

//        sessionId
//                |
//                ---> trace1
//                        |
//                        ---> span1 --> latency, tokens, cost, modelUsed,
//                        ---> span2 --> latency, tokens, cost, modelUsed,
//                        ---> span3 --> latency, tokens, cost, modelUsed,
//                ---> trace2
//                        |
//                        ---> span1 --> latency, tokens, cost, modelUsed,
//                        ---> span2 --> latency, tokens, cost, modelUsed,
//                        ---> span3 --> latency, tokens, cost, modelUsed,

        Map<String, SessionAnalyticsDto> sessionMap = new HashMap<>();

        for (Session session : user.getSessions()) {

            SessionAnalyticsDto sessionDto = sessionMap
                .computeIfAbsent(session.getSessionId(), id -> new SessionAnalyticsDto());

            for (Trace trace : session.getTraces()) {

                TraceAnalyticsDto traceDto = sessionDto.getTraceMap()
                    .computeIfAbsent( trace.getTraceId(), id -> new TraceAnalyticsDto());

                for (Span span : trace.getSpans()) {

                    if (!span.hasError() && !span.isCompleted()) {
                        SpanMetricsDto spanDto = new SpanMetricsDto();
                        spanDto.setLatencyMs(span.getLatencyMs());
                        spanDto.setTokensUsed(span.getTotalTokens());
                        spanDto.setCost(span.getCost());
                        spanDto.setModelUsed(span.getModel());

                        traceDto.setTotalLatency(traceDto.getTotalLatency() + span.getLatencyMs());
                        traceDto.setTotalTokens(traceDto.getTotalTokens() + span.getTotalTokens());
                        traceDto.setTotalCost(traceDto.getTotalCost() + span.getCost());
                        traceDto.getSpanMap().put(span.getSpanId(), spanDto);
                    }

                }
            }
        }

        return ResponseEntity.ok(sessionMap);
    }
}
