package com.backend_agent_obs.agent.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    USER("USER"), ADMIN("ADMIN");

    final String roleName;
    RoleName(String roleName) {
        this.roleName = roleName;
    }

}
