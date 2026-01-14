package com.backend_agent_obs.agent.auth.authentication;

import java.util.Collection;

import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.entities.entity.User;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

@Getter
public class ApiKeyHashAuthenticationToken extends AbstractAuthenticationToken {

    private final User user;
    private final String apiKeyHash;

    public ApiKeyHashAuthenticationToken(String apiKeyHash) {
        super((Collection<? extends GrantedAuthority>) null);
        this.apiKeyHash = apiKeyHash;
        this.user = null;
        setAuthenticated(false);
    }

    public ApiKeyHashAuthenticationToken(User user, String apiKeyHash, @Nullable Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.apiKeyHash = apiKeyHash;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return apiKeyHash;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return user;
    }

}
