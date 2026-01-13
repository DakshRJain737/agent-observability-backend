package com.backend_agent_obs.agent.controller;

import java.util.List;

import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.dto.userDto.UserLoginRequestDto;
import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.services.serviceImpl.UserServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> registerUser(@RequestBody UserRegisterDto userRegisterDto) {
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        return userService.userRegister(userRegisterDto);
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<UserDetailsDto>> findAllUsers() {
        return userService.findAllUsers();
    }

//    @GetMapping("/generate-token")
//    public ResponseEntity<String> login(@RequestBody UserLoginRequestDto userLoginRequestDto) {
//        return ResponseEntity.ok("Login Successful");
//    }
}
