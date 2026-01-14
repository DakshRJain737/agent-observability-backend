package com.backend_agent_obs.agent.auth.filter;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend_agent_obs.agent.auth.authentication.JWTAuthenticationToken;
import com.backend_agent_obs.agent.auth.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JWTRefreshFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JWTRefreshFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if(!request.getServletPath().equals("/refresh-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refreshToken = extractJwtFromRequest(request);
        if(refreshToken == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        JWTAuthenticationToken jwtAuthToken =  new JWTAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(jwtAuthToken);

        if(authResult.isAuthenticated()) {
            String newToken = JwtUtil.generateToken(authResult.getName(), 15);
            response.setHeader("Authorization", "Bearer " + newToken);
        }

    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            return null;
        }
        String refreshToken = null;
        for(Cookie cookie : cookies) {
            if(cookie.getName().equals("refreshToken")) {
                refreshToken = cookie.getValue();
                break;
            }
        }
        return refreshToken;
    }
}
