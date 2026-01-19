package com.backend_agent_obs.agent.services.service;

import org.springframework.http.ResponseEntity;

public interface AnalyticsService {

    ResponseEntity<?> getAllMetrics();
}
