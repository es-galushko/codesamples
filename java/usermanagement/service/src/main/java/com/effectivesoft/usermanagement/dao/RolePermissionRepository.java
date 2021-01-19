package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RolePermissionRepository  extends JpaRepository<RolePermission, Integer> {
    @Query("SELECT rp FROM RolePermission rp WHERE rp.role.id = :roleId AND rp.product.id = :productId AND rp.objectType = :objectType")
    List<RolePermission> findByRoleProductObjectType(@Param("roleId") Integer roleId, @Param("productId") Integer productId, @Param("objectType") String objectType);

}
