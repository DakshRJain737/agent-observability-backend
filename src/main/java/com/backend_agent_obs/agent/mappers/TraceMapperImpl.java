package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.TraceDto;
import com.backend_agent_obs.agent.entities.entity.Trace;
import org.springframework.stereotype.Service;

@Service
public class TraceMapperImpl {

    public static TraceDto fromTraceToTraceDto(Trace trace) {
        TraceDto traceDto = new TraceDto();
        traceDto.setTraceId(trace.getTraceId());
        traceDto.setName(trace.getName());
        traceDto.setMetadata(trace.getMetadata());
        traceDto.setSessionId(trace.getSession().getSessionId());
        traceDto.setUserId(trace.getUserId());
        traceDto.setStartTime(trace.getStartTime());
        traceDto.setEndTime(trace.getEndTime());
        return traceDto;
    }

    public static Trace fromTraceDtoToTrace(TraceDto traceDto) {
        Trace trace = new Trace();
        trace.setTraceId(traceDto.getTraceId());
        trace.setName(traceDto.getName());
        trace.setMetadata(traceDto.getMetadata());
        trace.setStartTime(traceDto.getStartTime());
        trace.setEndTime(traceDto.getEndTime());
        // sessionId, userId set in service
        return trace;
    }
}
