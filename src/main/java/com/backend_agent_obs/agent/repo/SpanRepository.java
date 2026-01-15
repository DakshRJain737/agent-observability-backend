package com.backend_agent_obs.agent.repo;

import java.util.Optional;

import com.backend_agent_obs.agent.entities.entity.Span;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpanRepository extends JpaRepository<Span, Long> {
    Optional<Span> findBySpanId(@NotBlank(message = "Parent span ID is required") String parentSpanId);
    boolean existsBySpanId(String spanId);
}
