package com.backend_agent_obs.agent.auth.provider;

import com.backend_agent_obs.agent.auth.authentication.ApiKeyHashAuthenticationToken;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.services.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class ApiKeyHashAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public ApiKeyHashAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public @Nullable Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        String apiKeyHash = ((ApiKeyHashAuthenticationToken) authentication).getApiKeyHash();
        User user = userService.loadUserByApiKeyHash(apiKeyHash);
        log.info(user.getUsername());
        return new ApiKeyHashAuthenticationToken(user,apiKeyHash,user.getAuthorities());
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return ApiKeyHashAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
