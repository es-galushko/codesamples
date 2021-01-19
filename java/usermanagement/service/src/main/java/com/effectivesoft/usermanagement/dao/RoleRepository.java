package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
