package com.effectivesoft.usermanagement.web.auth;

import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;


public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with username: " + username + " not found");
        }

        if (user.getAdmin() == null || !user.getAdmin()) {
            throw new UserNotAdminException("User with username: " + username + " not admin");
        }

        if (user.getLocked() != null && user.getLocked()) {
            if (userService.checkUserLocked(user)) {
                throw new UserLockedException("User with username: " + username + " locked");
            }
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                new ArrayList<SimpleGrantedAuthority>());
    }
}
