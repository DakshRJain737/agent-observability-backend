package com.backend_agent_obs.agent.repo;

import java.util.Optional;

import com.backend_agent_obs.agent.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend_agent_obs.agent.entities.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findByRoleName(RoleName role);
}
