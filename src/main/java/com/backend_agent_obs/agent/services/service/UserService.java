package com.backend_agent_obs.agent.services.service;

import java.util.List;

import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.entities.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    ResponseEntity<List<UserDetailsDto>> findAllUsers();

    ResponseEntity<String> userRegister(UserRegisterDto userRegisterDto);

}
