package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.PermissionObjectType;
import com.effectivesoft.usermanagement.entity.Product;
import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.RolePermission;

import java.util.List;

public interface RolePermissionService {
    List<RolePermission> findByRoleProductObjectType(Role role, Product product, PermissionObjectType type);
    List<RolePermission> save(List<RolePermission> rolePermissions);
}
