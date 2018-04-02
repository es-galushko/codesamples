package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionRepository extends JpaRepository<UserSession, Integer> {
}
