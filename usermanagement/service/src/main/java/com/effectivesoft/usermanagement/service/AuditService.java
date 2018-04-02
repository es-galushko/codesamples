package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Audit;
import com.effectivesoft.usermanagement.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface AuditService {
    Page<Audit> findAll(Pageable pageable);
    Page<Audit> findAllByUsername(Pageable pageable, String username);
    Page<Audit> findAllByEventName(Pageable pageable, String eventName);
    Page<Audit> findAllByEventDescription(Pageable pageable, String eventDescription);
    Page<Audit> findAllByCreatedAt(Pageable pageable, Date createdAtStart, Date createdAtEnd);
    void deleteAll();
    void addLockedUserAudit(User user);
}
