package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.entity.UserRole;

import java.util.List;

public interface UserRoleService {
    List<UserRole> findByUserRole(User user, Role role);
    List<UserRole> findByRole(Role role);
    void delete(UserRole userRole);
    List<UserRole> save(List<UserRole> userRoles);
}
