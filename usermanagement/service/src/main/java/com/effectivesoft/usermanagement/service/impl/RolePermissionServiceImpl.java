package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.RolePermissionRepository;
import com.effectivesoft.usermanagement.entity.PermissionObjectType;
import com.effectivesoft.usermanagement.entity.Product;
import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.RolePermission;
import com.effectivesoft.usermanagement.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {
    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Override
    public List<RolePermission> findByRoleProductObjectType(Role role, Product product, PermissionObjectType type) {
        return rolePermissionRepository.findByRoleProductObjectType(role.getId(), product.getId(), type.getName());
    }

    @Override
    public List<RolePermission> save(List<RolePermission> rolePermissions) {
        return rolePermissionRepository.save(rolePermissions);
    }

}
