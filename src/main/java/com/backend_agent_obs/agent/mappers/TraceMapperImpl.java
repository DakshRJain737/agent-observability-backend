package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.TraceRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.TraceResponseDto;
import com.backend_agent_obs.agent.entities.entity.Session;
import com.backend_agent_obs.agent.entities.entity.Trace;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class TraceMapperImpl {

    public static TraceResponseDto fromTraceToTraceResponseDto(Trace trace) {
        TraceResponseDto traceResponseDto = new TraceResponseDto();
        traceResponseDto.setTraceId(trace.getTraceId());
        traceResponseDto.setName(trace.getName());
        traceResponseDto.setMetadata(trace.getMetadata());if (trace.getSession() != null) {
            traceResponseDto.setSessionId(trace.getSession().getSessionId());
            traceResponseDto.setUserId(trace.getSession().getUserId());
        }
        traceResponseDto.setStartTime(trace.getStartTime());
        traceResponseDto.setEndTime(trace.getEndTime());
        return traceResponseDto;
    }

    public static Trace fromTraceResponseDtoToTrace(TraceResponseDto traceResponseDto) {
        if(traceResponseDto == null) return null;

        Trace trace = new Trace();
        trace.setTraceId(traceResponseDto.getTraceId());
        trace.setName(traceResponseDto.getName());
        trace.setMetadata(traceResponseDto.getMetadata());
        trace.setStartTime(traceResponseDto.getStartTime());
        trace.setEndTime(traceResponseDto.getEndTime());

        // sessionId, userId set in service
        return trace;
    }

    public static Trace fromTraceRequestDtoToTrace(TraceRequestDto traceRequestDto) {
        if (traceRequestDto == null) return null;

        Trace trace = new Trace();
        trace.setTraceId(traceRequestDto.getTraceId());
        trace.setName(traceRequestDto.getName());
        trace.setMetadata(traceRequestDto.getMetadata());
        return trace;
    }

    public static Session fromTraceRequestDtoToSession(TraceRequestDto traceRequestDto) {
        if (traceRequestDto == null) return null;

        Session session = new Session();
        session.setSessionId(traceRequestDto.getSessionId());
        session.setUserId(traceRequestDto.getUserId());
        return session;
    }

}
