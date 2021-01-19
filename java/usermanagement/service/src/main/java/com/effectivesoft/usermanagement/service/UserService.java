package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Role;
import com.effectivesoft.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;

public interface UserService {
    final static int MAX_INVALID_LOGIN_COUNT = 5;
    /**
     * period in minutes
     */
    final static long LOCKED_PERIOD = 60;

    User saveUser(User user);
    void saveUser(User user, List<Role> roles);
    boolean checkUserLocked(User user);
    void failureLogin(String username);
    void successLogin(String username);
    User createUser();
    User findByUsername(String username);
    User findByEmail(String email);
    User findOne(Integer id);
    List<User> findAll();
    List<User> findAll(Sort sort);
    Page<User> findAll(Pageable pageable);

    Page<User> findAllByUsername(Pageable pageable, String username);
    Page<User> findAllByDescription(Pageable pageable, String description);
    Page<User> findAllByEmail(Pageable pageable, String email);
    Page<User> findAllByLastLogonBetween(Pageable pageable, Date lastLogonStart, Date lastLogonEnd);
    Page<User> findAllByLocked(Pageable pageable, Boolean locked);
    Page<User> findAllByAdmin(Pageable pageable, Boolean admin);
}
