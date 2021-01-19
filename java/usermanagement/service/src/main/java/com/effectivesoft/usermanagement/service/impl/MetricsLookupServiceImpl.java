package com.effectivesoft.usermanagement.service.impl;

import com.effectivesoft.usermanagement.dao.MetricsLookupRepository;
import com.effectivesoft.usermanagement.entity.MetricsLookup;
import com.effectivesoft.usermanagement.service.MetricsLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetricsLookupServiceImpl implements MetricsLookupService {
    @Autowired
    private MetricsLookupRepository metricsLookupRepository;

    @Override
    public Page<MetricsLookup> findAll(Pageable pageable) {
        return metricsLookupRepository.findAll(pageable);
    }

    @Override
    public List<MetricsLookup> findAllByProductObjectType(Integer productId, String objectType) {
        return metricsLookupRepository.findAllByProductObjectType(productId, objectType);
    }

    @Override
    public MetricsLookup findOne(Integer id) {
        return metricsLookupRepository.findOne(id);
    }
}
