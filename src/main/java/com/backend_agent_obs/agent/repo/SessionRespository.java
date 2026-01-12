package com.backend_agent_obs.agent.repo;

import java.util.Optional;

import com.backend_agent_obs.agent.entities.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRespository extends JpaRepository<Session, Long> {

    Optional<Session> findBySessionId(String sessionId);
}
