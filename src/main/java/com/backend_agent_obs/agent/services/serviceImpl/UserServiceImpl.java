package com.backend_agent_obs.agent.services.serviceImpl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.backend_agent_obs.agent.auth.util.ApiKeyHashUtil;
import com.backend_agent_obs.agent.dto.userDto.UserDetailsDto;
import com.backend_agent_obs.agent.dto.userDto.UserRegisterDto;
import com.backend_agent_obs.agent.entities.entity.Role;
import com.backend_agent_obs.agent.entities.entity.User;
import com.backend_agent_obs.agent.enums.RoleName;
import com.backend_agent_obs.agent.mappers.UserMapperImpl;
import com.backend_agent_obs.agent.repo.RoleRepository;
import com.backend_agent_obs.agent.repo.UserRepository;
import com.backend_agent_obs.agent.services.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    final private UserRepository userRepository;
    final private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public ResponseEntity<List<UserDetailsDto>> findAllUsers() {
        Pageable pageable = PageRequest.of(0,20, Sort.by("username").ascending());
        Page<User> page = userRepository.findAll(pageable);
        log.info("20 User data fetched");
        List<UserDetailsDto> userDetailsList = page.stream().map(UserMapperImpl::fromUserToUserDetailsDto).toList();
        log.info("20 users data sent");
        return ResponseEntity.ok(userDetailsList);
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
        Role role = roleRepository.findByRoleName(RoleName.USER).get();
        user.addRole(role);
        userRepository.save(user);
        log.info("User registered with username {}", user.getUsername());
        return ResponseEntity.ok().body("User registered successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
