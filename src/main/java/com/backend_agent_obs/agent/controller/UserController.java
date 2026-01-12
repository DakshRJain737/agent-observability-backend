package com.backend_agent_obs.agent.controller;

import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.services.serviceImpl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    final private BCryptPasswordEncoder passwordEncoder;
    final private UserServiceImpl userService;

    public UserController(BCryptPasswordEncoder passwordEncoder, UserServiceImpl userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(UserRegisterDto userRegisterDto) {
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return userService.userRegister(userRegisterDto);
    }
}
