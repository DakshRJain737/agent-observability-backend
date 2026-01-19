package com.backend_agent_obs.agent.auth.authentication;

import java.util.Collection;

import com.backend_agent_obs.agent.auth.util.JwtUtil;
import com.backend_agent_obs.agent.entities.entity.User;
import lombok.Getter;
import org.hibernate.engine.internal.Nullability;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class JWTAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;

    public JWTAuthenticationToken(String token) {
        super((Collection<? extends GrantedAuthority>) null);
        this.token = token;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

}
