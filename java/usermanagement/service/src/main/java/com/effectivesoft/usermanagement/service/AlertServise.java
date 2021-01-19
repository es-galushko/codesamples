package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertServise {
    Alert findOne(Integer id);
    Alert save(Alert alert);
    void delete(Alert alert);
    Page<Alert> findAll(Pageable pageable);
    Page<Alert> findAllByName(Pageable pageable, String name);
    Page<Alert> findAllByProduct(Pageable pageable, Integer productId);
    Page<Alert> findAllByObjectType(Pageable pageable, String objectType);
    Page<Alert> findAllByEnabled(Pageable pageable, Boolean enabled);
    Page<Alert> findAllByTriggered(Pageable pageable, Boolean triggered);
    Page<Alert> findAllByRule(Pageable pageable, String metricName, String operator, String threshold, String fullRule);
}
