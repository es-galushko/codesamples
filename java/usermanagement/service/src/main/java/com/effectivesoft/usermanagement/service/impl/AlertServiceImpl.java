package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.AlertRepository;
import com.effectivesoft.usermanagement.entity.Alert;
import com.effectivesoft.usermanagement.service.AlertServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlertServiceImpl implements AlertServise {
    @Autowired
    private AlertRepository alertRepository;

    @Override
    public Alert findOne(Integer id) {
        return alertRepository.findOne(id);
    }

    @Override
    public Alert save(Alert alert) {
        return alertRepository.save(alert);
    }

    @Override
    public void delete(Alert alert) {
        alertRepository.delete(alert);
    }

    @Override
    public Page<Alert> findAll(Pageable pageable) {
        return alertRepository.findAll(pageable);
    }

    @Override
    public Page<Alert> findAllByName(Pageable pageable, String name) {
        return alertRepository.findAllByName(pageable, name);
    }

    @Override
    public Page<Alert> findAllByProduct(Pageable pageable, Integer productId) {
        return alertRepository.findAllByProduct(pageable, productId);
    }

    @Override
    public Page<Alert> findAllByObjectType(Pageable pageable, String objectType) {
        return alertRepository.findAllByObjectType(pageable, objectType);
    }

    @Override
    public Page<Alert> findAllByEnabled(Pageable pageable, Boolean enabled) {
        return alertRepository.findAllByEnabled(pageable, enabled);
    }

    @Override
    public Page<Alert> findAllByTriggered(Pageable pageable, Boolean triggered) {
        return alertRepository.findAllByTriggered(pageable, triggered);
    }

    @Override
    public Page<Alert> findAllByRule(Pageable pageable, String metricName, String operator, String threshold, String fullRule) {
        return alertRepository.findAllByRule(pageable, metricName, operator, threshold, fullRule);
    }

}
