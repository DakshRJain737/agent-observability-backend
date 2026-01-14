package com.backend_agent_obs.agent.repo;

import java.time.LocalDateTime;
import java.util.Optional;

import com.backend_agent_obs.agent.entities.entity.Trace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TraceRepository extends JpaRepository<Trace, Long> {

    Optional<Trace> findByTraceId(String traceId);

    @Query("""
            SELECT t FROM Trace t
            WHERE t.session.sessionId = :sessionId
            AND t.session.userId = :userId
            AND t.startTime > :startTime
            AND t.endTime IS NOT NULL
            AND t.endTime < :endTime
            """)
    Page<Trace> findTracesBySessionAndUserAndTimeRange(
            @Param("sessionId") String sessionId,
            @Param("userId") String userId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable);
}
