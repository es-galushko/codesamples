package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.UserRepository;
import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.entity.UserRole;
import com.effectivesoft.usermanagement.service.AuditService;
import com.effectivesoft.usermanagement.service.UserRoleService;
import com.effectivesoft.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final long MILLISEC_IN_MIN = 60 * 1000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserRoleService userRoleService;

    @Transactional
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUser(User user, List<Role> selectedRoles) {
        user = userRepository.save(user);

        List<UserRole> currentUserRoles = user.getUserRoles();

        List<Role> currentRoles = new ArrayList<Role>();
        for (UserRole userRole : currentUserRoles){
            currentRoles.add(userRole.getRole());
        }

        List<UserRole> attachedUserRoles = new ArrayList<UserRole>();
        for(Role role : selectedRoles){
            if (!currentRoles.contains(role)){
                UserRole userRole = new UserRole();
                userRole.setRole(role);
                userRole.setUser(user);
                attachedUserRoles.add(userRole);
            }
        }
        userRoleService.save(attachedUserRoles);

        for(Role role : currentRoles){
            if(!selectedRoles.contains(role)){
                List<UserRole> userRoles = userRoleService.findByUserRole(user, role);
                for (UserRole userRole : userRoles){
                    userRoleService.delete(userRole);
                }
            }
        }
    }

    @Override
    public boolean checkUserLocked(User user) {
        Date currentDate = new Date();
        Date userLockedDate = user.getLastLogon();
        if (userLockedDate == null) {
            return false;
        }

        long userLockedPeriodMilliseconds = currentDate.getTime() - userLockedDate.getTime();
        long userLockedPeriodMinutes = userLockedPeriodMilliseconds / MILLISEC_IN_MIN;

        if (userLockedPeriodMinutes > LOCKED_PERIOD) {
            user.setLocked(false);
            user.setInvalidLoginCount(0);
            userRepository.save(user);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void failureLogin(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            return;
        }

        Integer userInvalidLoginCount = user.getInvalidLoginCount();
        if (userInvalidLoginCount == null) {
            userInvalidLoginCount = 1;
        } else {
            userInvalidLoginCount += 1;
        }

        user.setInvalidLoginCount(userInvalidLoginCount);
        if (userInvalidLoginCount >= MAX_INVALID_LOGIN_COUNT) {
            user.setLocked(true);
            auditService.addLockedUserAudit(user);
        }
        user.setLastLogon(new Date());

        userRepository.save(user);
    }

    @Override
    public void successLogin(String username) {
        User user = userRepository.findByUsername(username);
        user.setInvalidLoginCount(0);
        user.setLastLogon(new Date());
        userRepository.save(user);
    }

    @Override
    public User createUser() {
        User user = new User();
        user.setAdmin(true);
        user.setCreatedAt(new Date());
        user.setCreatedBy("admin");
        user.setDescription("description");
        user.setEmail("email");
        user.setLastLogon(new Date());
        user.setLocked(false);
        user.setPassword("password");
        user.setStatus("status");
        user.setUpdatedAt(new Date());
        user.setUpdatedBy("user");
        user.setUsername("username");

        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User findOne(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> findAllByUsername(Pageable pageable, String username) {
        return userRepository.findAllByUsername(pageable, username);
    }

    @Override
    public Page<User> findAllByDescription(Pageable pageable, String description) {
        return userRepository.findAllByDescription(pageable, description);
    }

    @Override
    public Page<User> findAllByEmail(Pageable pageable, String email) {
        return userRepository.findAllByEmail(pageable, email);
    }

    @Override
    public Page<User> findAllByLastLogonBetween(Pageable pageable, Date lastLogonStart, Date lastLogonEnd) {
        return userRepository.findAllByLastLogonBetween(pageable, lastLogonStart, lastLogonEnd);
    }

    @Override
    public Page<User> findAllByLocked(Pageable pageable, Boolean locked) {
        return userRepository.findAllByLocked(pageable, locked);
    }

    @Override
    public Page<User> findAllByAdmin(Pageable pageable, Boolean admin) {
        return userRepository.findAllByAdmin(pageable, admin);
    }
}
