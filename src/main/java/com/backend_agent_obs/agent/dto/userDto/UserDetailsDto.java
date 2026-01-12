package com.backend_agent_obs.agent.dto.userDto;

import lombok.Data;

@Data
public class UserDetailsDto {
    private String username;
    private String email;
    private String apiKeyHash;
    private String organizationId;
}
