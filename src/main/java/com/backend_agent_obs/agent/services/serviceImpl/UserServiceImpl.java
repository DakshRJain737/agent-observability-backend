package com.backend_agent_obs.agent.services.serviceImpl;

import java.util.List;
import java.util.Optional;

import com.backend_agent_obs.agent.auth.util.ApiKeyHashUtil;
import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.entities.entity.Role;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.enums.RoleName;
import com.backend_agent_obs.agent.mappers.UserMapperImpl;
import com.backend_agent_obs.agent.repo.UserRepository;
import com.backend_agent_obs.agent.services.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> saveUser(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDetailsDto> findAllUsers() {
        return List.of();
    }

    @Override
    @Transactional
    public ResponseEntity<String> userRegister(UserRegisterDto userRegisterDto) {
        Optional<User> user1 = userRepository.findByUsername(userRegisterDto.getUsername());
        if(user1.isPresent()) {
            return ResponseEntity.badRequest().body("User with the following username already exists");
        }
        Optional<User> user2 = userRepository.findByEmail(userRegisterDto.getEmail());
        if(user2.isPresent()) {
            return ResponseEntity.badRequest().body("User with the following email already exists");
        }
        User user = UserMapperImpl.fromUserRegisterDtoToUser(userRegisterDto);
        user.setApiKeyHash(ApiKeyHashUtil.hashApiKey(ApiKeyHashUtil.generateApiKey()));
        user.addRole(new Role(RoleName.USER));
        userRepository.save(user);
        return ResponseEntity.ok().body("User registered successfully");
    }

    @Override
    public ResponseEntity<String> login(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
