package com.backend_agent_obs.agent.repo;

import com.backend_agent_obs.agent.entities.entity.Span;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpanRepository extends JpaRepository<Span, Long> {
}
