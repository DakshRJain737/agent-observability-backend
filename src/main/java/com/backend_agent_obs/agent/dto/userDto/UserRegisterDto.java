package com.backend_agent_obs.agent.dto.userDto;

import lombok.Data;

@Data
public class UserRegisterDto {
    private String username;
    private String email;
    private String password;
    private String organizationId;
}
