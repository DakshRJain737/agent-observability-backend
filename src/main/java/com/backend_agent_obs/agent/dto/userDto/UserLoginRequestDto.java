package com.backend_agent_obs.agent.dto.userDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequestDto {

    private String username;
    private String password;

    public UserLoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserLoginRequestDto() {
    }

}
