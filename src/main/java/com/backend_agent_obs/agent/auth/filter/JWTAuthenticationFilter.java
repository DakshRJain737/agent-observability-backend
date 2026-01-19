package com.backend_agent_obs.agent.auth.filter;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend_agent_obs.agent.dto.userDto.UserLoginRequestDto;
import com.backend_agent_obs.agent.auth.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null
            && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
        filterChain.doFilter(request, response);
        return;
    }
        if(!request.getServletPath().equals("/user/generate-token")) {
            filterChain.doFilter(request,response);
            return;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        UserLoginRequestDto login = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(),login.getPassword());
        Authentication authentication = authenticationManager.authenticate(authToken);

        if(authentication.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtil.generateToken(authentication.getName(), 15);
            log.info("JWT Token for {} : {}", login.getUsername(), token);
            response.setHeader("Authorization", "Bearer " + token);

            String refreshToken = JwtUtil.generateToken(authentication.getName(), 7 * 24 * 60);
            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/refresh-token");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);
            response.addCookie(refreshCookie);
        }
    }
}
