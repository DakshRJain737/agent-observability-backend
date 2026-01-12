package com.backend_agent_obs.agent.auth.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.backend_agent_obs.agent.auth.authentication.JWTAuthenticationToken;
import com.backend_agent_obs.agent.auth.util.JwtUtil;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;

    public JWTAuthenticationProvider(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = ((JWTAuthenticationToken) authentication).getToken();
        String username = jwtUtil.validateAndExtractUsername(token);
        if(username == null) {
            throw new BadCredentialsException("Invalid JWT token");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails,
                null,
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JWTAuthenticationProvider.class.isAssignableFrom(authentication);
    }
}
