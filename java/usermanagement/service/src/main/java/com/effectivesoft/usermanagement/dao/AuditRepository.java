package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.Audit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;

import javax.persistence.TemporalType;
import java.util.Date;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
    Page<Audit> findAllByUsername(Pageable pageable, String username);
    Page<Audit> findAllByEventName(Pageable pageable, String eventName);

    @Query("SELECT a FROM Audit a WHERE a.eventDescription LIKE %:eventDescriptionKey%")
    Page<Audit> findAllByEventDescriptionKey(Pageable pageable, @Param("eventDescriptionKey") String eventDescriptionKey);

    Page<Audit> findAllByCreatedAtBetween(Pageable pageable, @Temporal(TemporalType.TIMESTAMP) Date createdAtStart, @Temporal(TemporalType.TIMESTAMP) Date createdAtEnd);

}
