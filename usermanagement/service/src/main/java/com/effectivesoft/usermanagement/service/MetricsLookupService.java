package com.effectivesoft.usermanagement.service;

import com.effectivesoft.usermanagement.entity.MetricsLookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MetricsLookupService {
    Page<MetricsLookup> findAll(Pageable pageable);
    List<MetricsLookup> findAllByProductObjectType(Integer productId, String objectType);
    MetricsLookup findOne(Integer id);
}
