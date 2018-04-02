package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.UserRoleRepository;
import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.entity.UserRole;
import com.effectivesoft.usermanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public List<UserRole> findByUserRole(User user, Role role) {
        return userRoleRepository.findByUserRole(user.getId(), role.getId());
    }

    @Override
    public List<UserRole> findByRole(Role role) {
        return userRoleRepository.findByRole(role.getId());
    }

    @Override
    public void delete(UserRole userRole) {
        userRoleRepository.delete(userRole.getId());
    }

    @Override
    public List<UserRole> save(List<UserRole> userRoles) {
        return userRoleRepository.save(userRoles);
    }
}
