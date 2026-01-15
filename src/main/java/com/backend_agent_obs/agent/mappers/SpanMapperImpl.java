package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.ErrorDto;
import com.backend_agent_obs.agent.dto.entityDto.SpanRequestDto;
import com.backend_agent_obs.agent.dto.entityDto.SpanResponseDto;
import com.backend_agent_obs.agent.entities.entity.Span;
import com.backend_agent_obs.agent.entities.entity.Trace;
import com.backend_agent_obs.agent.enums.SpanStatus;

public class SpanMapperImpl {

    public static SpanResponseDto fromSpanToSpanResponseDto(Span span) {
        SpanResponseDto spanResponseDto = new SpanResponseDto();
        spanResponseDto.setSpanId(span.getSpanId());
        spanResponseDto.setName(span.getName());
        spanResponseDto.setModel(span.getModel());
        spanResponseDto.setType(span.getType());
        spanResponseDto.setInput(span.getInput());
        spanResponseDto.setOutput(span.getOutput());
        spanResponseDto.setInputTokens(span.getInputTokens());
        spanResponseDto.setOutputTokens(span.getOutputTokens());
        spanResponseDto.setTotalTokens(span.getTotalTokens());
        spanResponseDto.setCost(span.getCost());
        spanResponseDto.setLatencyMs(span.getLatencyMs());
        spanResponseDto.setStartTime(span.getStartTime());
        spanResponseDto.setEndTime(span.getEndTime());
        if (span.getErrorMessage() != null) {
            ErrorDto errorDto = new ErrorDto(span.getErrorType(),span.getErrorMessage(),span.getErrorStackTrace());
            spanResponseDto.setError(errorDto);
        }
        else {
            spanResponseDto.setError(null);
        }
        spanResponseDto.setSpanStatus(span.getStatus().getDescription());
        spanResponseDto.setVersion(span.getVersion());
        spanResponseDto.setEnvironment(span.getEnvironment());
        spanResponseDto.setMetadata(span.getMetadata());
        if(span.getTrace() != null) {
            spanResponseDto.setTraceId(span.getTrace().getTraceId());
            if(span.getTrace().getSession() != null) {
                spanResponseDto.setSessionId(span.getTrace().getSession().getSessionId());
            }
        }
        return spanResponseDto;
    }

    public static Span fromSpanRequestDtoToSpan(SpanRequestDto spanRequestDto) {
        Span span = new Span();
        span.setSpanId(spanRequestDto.getSpanId());
        // parentSpan, traceId, sessionId set in service
        span.setName(spanRequestDto.getName());
        span.setModel(spanRequestDto.getModel());
        span.setType(spanRequestDto.getType());
        span.setInput(spanRequestDto.getInput());
        span.setOutput(spanRequestDto.getOutput());
        span.setInputTokens(spanRequestDto.getInputTokens());
        span.setOutputTokens(spanRequestDto.getOutputTokens());
        span.setTotalTokens(spanRequestDto.getTotalTokens());
        span.setCost(spanRequestDto.getCost());
        span.setLatencyMs(spanRequestDto.getLatencyMs());
        span.setStartTime(spanRequestDto.getStartTime());
        if (spanRequestDto.getError() != null) {
            span.setErrorType(spanRequestDto.getError().getErrorType());
            span.setErrorMessage(spanRequestDto.getError().getErrorMessage());
            span.setErrorStackTrace(spanRequestDto.getError().getErrorStackTrace());
        }

        if (spanRequestDto.getSpanStatus() != null) {
            span.setStatus(SpanStatus.valueOf(spanRequestDto.getSpanStatus().toUpperCase()));
        } else if (spanRequestDto.getError() != null && spanRequestDto.getError().getErrorType() != null) {
            span.setStatus(SpanStatus.ERROR);
        }

        span.setVersion(spanRequestDto.getVersion());
        span.setEnvironment(spanRequestDto.getEnvironment());
        span.setMetadata(spanRequestDto.getMetadata());

        return span;
    }

    public static Trace fromSpanRequestDtoToTrace(SpanRequestDto spanRequestDto) {
        if (spanRequestDto == null) return null;

        Trace trace = new Trace();
        trace.setTraceId(spanRequestDto.getTraceId());
        trace.setName(spanRequestDto.getName());
        trace.setMetadata(spanRequestDto.getMetadata());
        return trace;
        // sessionId, customer set in service
    }
}
