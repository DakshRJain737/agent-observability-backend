package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.entityDto.SpanDto;
import com.backend_agent_obs.agent.entities.entity.Span;

public class SpanMapperImpl {

    public static SpanDto fromSpanToSpanDto(Span span) {
        SpanDto spanDto = new SpanDto();
        spanDto.setSpanId(span.getSpanId());
        spanDto.setParentSpanId(span.getParentSpan().getSpanId());
        spanDto.setTraceId(span.getTrace().getTraceId());
        spanDto.setSessionId(span.getTrace().getSession().getSessionId());
        spanDto.setName(span.getName());
        spanDto.setModel(span.getModel());
        spanDto.setType(span.getType());
        spanDto.setInput(span.getInput());
        spanDto.setOutput(span.getOutput());
        spanDto.setInputTokens(span.getInputTokens());
        spanDto.setOutputTokens(span.getOutputTokens());
        spanDto.setTotalTokens(span.getTotalTokens());
        spanDto.setCost(span.getCost());
        spanDto.setLatencyMs(span.getLatencyMs());
        spanDto.setStartTime(span.getStartTime());
        spanDto.setEndTime(span.getEndTime());
        spanDto.setError(ErrorMapperImpl.fromErrorDetailsToErrorDto(span.getErrorType(), span.getErrorMessage(), span.getErrorStackTrace()));
        spanDto.setStatus(span.getStatus());
        spanDto.setVersion(span.getVersion());
        spanDto.setEnvironment(span.getEnvironment());
        spanDto.setMetadata(span.getMetadata());

        return spanDto;
    }

    public static Span fromSpanDtoToSpan(SpanDto spanDto) {
        Span span = new Span();
        span.setSpanId(spanDto.getSpanId());
        // parentSpan, traceId, sessionId set in service
        span.setName(spanDto.getName());
        span.setModel(spanDto.getModel());
        span.setType(spanDto.getType());
        span.setInput(spanDto.getInput());
        span.setOutput(spanDto.getOutput());
        span.setInputTokens(spanDto.getInputTokens());
        span.setOutputTokens(spanDto.getOutputTokens());
        span.setTotalTokens(spanDto.getTotalTokens());
        span.setCost(spanDto.getCost());
        span.setLatencyMs(spanDto.getLatencyMs());
        span.setStartTime(spanDto.getStartTime());
        span.setEndTime(spanDto.getEndTime());
        span.setErrorType(spanDto.getError().getErrorType());
        span.setErrorMessage(spanDto.getError().getErrorMessage());
        span.setErrorStackTrace(spanDto.getError().getErrorStackTrace());
        span.setStatus(spanDto.getStatus());
        span.setVersion(spanDto.getVersion());
        span.setEnvironment(spanDto.getEnvironment());
        span.setMetadata(spanDto.getMetadata());

        return span;
    }
}
