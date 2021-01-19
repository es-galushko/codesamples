package com.effectivesoft.usermanagement.web.auth;

import org.springframework.security.core.AuthenticationException;

public class UserNotAdminException extends AuthenticationException {
    public UserNotAdminException(String msg) {
        super(msg);
    }
}
