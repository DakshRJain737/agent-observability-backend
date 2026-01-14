package com.backend_agent_obs.agent.config;

import java.util.Arrays;

import com.backend_agent_obs.agent.auth.filter.ApiKeyHashAuthenticationFilter;
import com.backend_agent_obs.agent.auth.provider.ApiKeyHashAuthenticationProvider;
import com.backend_agent_obs.agent.services.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.backend_agent_obs.agent.auth.filter.JWTAuthenticationFilter;
import com.backend_agent_obs.agent.auth.filter.JWTRefreshFilter;
import com.backend_agent_obs.agent.auth.filter.JWTValidationFilter;
import com.backend_agent_obs.agent.auth.provider.JWTAuthenticationProvider;
import com.backend_agent_obs.agent.auth.util.JwtUtil;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public SecurityConfig(UserDetailsService userDetailsService, UserService userService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager) throws Exception {
        JWTAuthenticationFilter jwtAuthFilter =
                new JWTAuthenticationFilter(authenticationManager);
        JWTValidationFilter jwtValidationFilter =
                new JWTValidationFilter(authenticationManager);
        JWTRefreshFilter jwtRefreshFilter =
                new JWTRefreshFilter(authenticationManager);
        ApiKeyHashAuthenticationFilter apiKeyHashAuthenticationFilter =
                new ApiKeyHashAuthenticationFilter(authenticationManager);

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/register", "/user/generate-token", "/refresh-token", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter, JWTAuthenticationFilter.class)
                .addFilterAfter(jwtRefreshFilter, JWTValidationFilter.class)
                .addFilterBefore(apiKeyHashAuthenticationFilter, JWTAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                daoAuthenticationProvider(),
                jwtAuthenticationProvider(),
                apiKeyHashAuthenticationProvider()
        ));
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider() {
        return new JWTAuthenticationProvider(userDetailsService);
    }

    @Bean
    public ApiKeyHashAuthenticationProvider apiKeyHashAuthenticationProvider() {
        return new ApiKeyHashAuthenticationProvider(userService);
    }
}
