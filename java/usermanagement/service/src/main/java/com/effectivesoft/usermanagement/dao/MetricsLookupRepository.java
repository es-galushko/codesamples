package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.MetricsLookup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MetricsLookupRepository extends JpaRepository<MetricsLookup, Integer> {
    @Query("SELECT ml FROM MetricsLookup ml WHERE ml.product.id = :productId AND ml.objectType = :objectType")
    List<MetricsLookup> findAllByProductObjectType(@Param("productId") Integer productId, @Param("objectType") String objectType);
}
