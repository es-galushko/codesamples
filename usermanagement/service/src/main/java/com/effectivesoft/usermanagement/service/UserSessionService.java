package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.UserSession;

import java.util.List;

public interface UserSessionService {
    List<UserSession> save(List<UserSession> userSessions);
}
