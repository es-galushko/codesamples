package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.AuditRepository;
import com.effectivesoft.usermanagement.entity.Audit;
import com.effectivesoft.usermanagement.entity.AuditEventType;
import com.effectivesoft.usermanagement.entity.User;
import com.effectivesoft.usermanagement.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;

@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Page<Audit> findAll(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    @Override
    public Page<Audit> findAllByUsername(Pageable pageable, String username) {
        return auditRepository.findAllByUsername(pageable, username);
    }

    @Override
    public Page<Audit> findAllByEventName(Pageable pageable, String eventName) {
        return auditRepository.findAllByEventName(pageable, eventName);
    }

    @Override
    public Page<Audit> findAllByEventDescription(Pageable pageable, String eventDescription) {
        return auditRepository.findAllByEventDescriptionKey(pageable, eventDescription);
    }

    @Override
    public Page<Audit> findAllByCreatedAt(Pageable pageable, Date createdAtStart, Date createdAtEnd) {
        //todo
        return auditRepository.findAllByCreatedAtBetween(pageable, createdAtStart, createdAtEnd);
    }

    @Override
    public void deleteAll() {
        auditRepository.deleteAll();
    }

    @Override
    public void addLockedUserAudit(User user) {
        Audit audit = new Audit();
        audit.setUsername(user.getUsername());
        audit.setCreatedAt(new Date());
        audit.setEventDescription(MessageFormat.format(AuditEventType.USER_LOCKOUT.getAuditMessage(), user.getUsername(), user.getInvalidLoginCount()));
        audit.setEventName(AuditEventType.USER_LOCKOUT.getEventName());

        auditRepository.save(audit);
    }


}
