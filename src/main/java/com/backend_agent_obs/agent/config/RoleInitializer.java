package com.backend_agent_obs.agent.config;

import com.backend_agent_obs.agent.entities.entity.Role;
import com.backend_agent_obs.agent.enums.RoleName;
import com.backend_agent_obs.agent.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {

    private RoleRepository roleRepository;

    public RoleInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        for(RoleName roleName : RoleName.values()) {
            roleRepository.findByRoleName(roleName).orElseGet( () -> {
                Role role = new Role(roleName);
                return roleRepository.save(role);
            });
        }
    }
}
