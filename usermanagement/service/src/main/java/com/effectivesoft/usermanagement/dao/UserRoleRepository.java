package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.user.id = :userId AND ur.role.id = :roleId")
    List<UserRole> findByUserRole(@Param("userId") Integer userId, @Param("roleId") Integer roleId);
    @Query("SELECT ur FROM UserRole ur WHERE ur.role.id = :roleId")
    List<UserRole> findByRole(@Param("roleId") Integer roleId);
}
