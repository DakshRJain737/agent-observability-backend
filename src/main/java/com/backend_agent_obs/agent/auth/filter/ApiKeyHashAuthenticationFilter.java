package com.backend_agent_obs.agent.auth.filter;

import java.io.IOException;

import com.backend_agent_obs.agent.auth.authentication.ApiKeyHashAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
@Slf4j
public class ApiKeyHashAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public ApiKeyHashAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Servlet path : {}" ,request.getServletPath());
        if(!request.getServletPath().equals("/api/data/traces")) {
            filterChain.doFilter(request,response);
            return;
        }
        String apiKeyHash = extractApiKeyHashFromHeader(request);
        if (apiKeyHash == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"API key required for this endpoint\"}");
            return;
        }
        log.info("ApiKeyHash is valid, ApiKeyHash : {}" ,apiKeyHash);
        try {
            ApiKeyHashAuthenticationToken apiKeyHashAuthenticationToken =
                new ApiKeyHashAuthenticationToken(apiKeyHash);
            Authentication authentication =
                authenticationManager.authenticate(apiKeyHashAuthenticationToken);
            log.info(String.valueOf(authentication.isAuthenticated()));
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                filterChain.doFilter(request, response);
            }
            else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Invalid API key\"}");
            }
        }
        catch (Exception e) {
            log.error("API key authentication failed", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Authentication failed\"}");
        }
    }

    private String extractApiKeyHashFromHeader(HttpServletRequest request) {
        String apiKeyHash = request.getHeader("Authorization");
        if(apiKeyHash != null && apiKeyHash.startsWith("Bearer")) {
            return apiKeyHash.substring(7);
        }
        return null;
    }
}
