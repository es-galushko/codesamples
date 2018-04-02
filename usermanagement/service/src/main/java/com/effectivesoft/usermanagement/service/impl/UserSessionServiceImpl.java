package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.UserSessionRepository;
import com.effectivesoft.usermanagement.entity.UserSession;
import com.effectivesoft.usermanagement.service.UserSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSessionServiceImpl implements UserSessionService {
    @Autowired
    private UserSessionRepository userSessionRepository;

    @Override
    public List<UserSession> save(List<UserSession> userSessions) {
        return userSessionRepository.save(userSessions);
    }
}
