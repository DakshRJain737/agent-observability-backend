package com.backend_agent_obs.agent.mappers;

import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.entities.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapperImpl {

    public static User fromUserRegisterDtoToUser(UserRegisterDto userRegisterDto) {
        User user = new User();
        log.info("UserRegisterDto ---> User");
        user.setUsername(userRegisterDto.getUsername());
        user.setEmail(userRegisterDto.getEmail());
        user.setPassword(userRegisterDto.getPassword());
        user.setOrganizationId(userRegisterDto.getOrganizationId());
        return user;
    }

    public static UserDetailsDto fromUserToUserDetailsDto(User user) {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        log.info("User ---> UserDetailsDto");
        userDetailsDto.setUsername(user.getUsername());
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setApiKeyHash(user.getApiKeyHash());
        userDetailsDto.setOrganizationId(user.getOrganizationId());

        return userDetailsDto;
    }
}
