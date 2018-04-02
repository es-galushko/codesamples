package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RoleService {
    void delete(Role role);
    Role findOne(Integer id);
    Role findByName(String name);
    Role save(Role role);
    Page<Role> findAll(Pageable pageable);
    List<Role> findAll(Sort sort);
    List<Role> findAll();
}
