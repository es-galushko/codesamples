package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.RoleRepository;
import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.service.RoleService;
import com.effectivesoft.usermanagement.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public List<Role> findAll(Sort sort) {
        return roleRepository.findAll(sort);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void delete(Role role) {
        roleRepository.delete(role);
    }

    @Override
    public Role findOne(Integer id) {
        return roleRepository.findOne(id);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
