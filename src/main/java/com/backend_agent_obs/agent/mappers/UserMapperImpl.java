package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.entities.entity.User;

public class UserMapperImpl {

    public static User fromUserRegisterDtoToUser(UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getPassword());
        user.setPassword(userRegisterDto.getPassword());
        user.setOrganizationId(userRegisterDto.getOrganizationId());
        return user;
    }
}
