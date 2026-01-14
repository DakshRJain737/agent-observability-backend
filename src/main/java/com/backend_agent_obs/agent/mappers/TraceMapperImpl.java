package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.TraceRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceResponseDto;
import com.backend_agent_obs.agent.entities.entity.Trace;
import org.springframework.stereotype.Service;

@Service
public class TraceMapperImpl {

    public static TraceResponseDto fromTraceToTraceResponseDto(Trace trace) {
        TraceResponseDto traceResponseDto = new TraceResponseDto();
        traceResponseDto.setTraceId(trace.getTraceId());
        traceResponseDto.setName(trace.getName());
        traceResponseDto.setMetadata(trace.getMetadata());
        traceResponseDto.setSessionId(trace.getSession().getSessionId());
        traceResponseDto.setUserId(trace.getUserId());
        traceResponseDto.setStartTime(trace.getStartTime());
        traceResponseDto.setEndTime(trace.getEndTime());
        return traceResponseDto;
    }

    public static Trace fromTraceResponseDtoToTrace(TraceResponseDto traceResponseDto) {
        Trace trace = new Trace();
        trace.setTraceId(traceResponseDto.getTraceId());
        trace.setName(traceResponseDto.getName());
        trace.setMetadata(traceResponseDto.getMetadata());
        trace.setStartTime(traceResponseDto.getStartTime());
        trace.setEndTime(traceResponseDto.getEndTime());
        trace.getSession().setUserId(traceResponseDto.getUserId());
        // sessionId, userId set in service
        return trace;
    }

    public static Trace fromTraceRequestDtoToTrace(TraceRequestDto traceRequestDto) {
        Trace trace = new Trace();
        trace.setTraceId(traceRequestDto.getTraceId());
        trace.setName(traceRequestDto.getName());
        trace.setMetadata(traceRequestDto.getMetadata());
        // sessionId, userId set in service
        return trace;
    }
}
