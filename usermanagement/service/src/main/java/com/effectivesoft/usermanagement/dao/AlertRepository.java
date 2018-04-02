package com.effectivesoft.usermanagement.dao;

import com.effectivesoft.usermanagement.entity.Alert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlertRepository extends JpaRepository<Alert, Integer> {
    @Query("SELECT a FROM Alert a WHERE a.name LIKE %:nameKey%")
    Page<Alert> findAllByName(Pageable pageable, @Param("nameKey") String nameKey);

    @Query("SELECT a FROM Alert a WHERE a.product.id = :productId")
    Page<Alert> findAllByProduct(Pageable pageable, @Param("productId") Integer productId);

    Page<Alert> findAllByObjectType(Pageable pageable, String objectType);
    Page<Alert> findAllByEnabled(Pageable pageable, Boolean enabled);
    Page<Alert> findAllByTriggered(Pageable pageable, Boolean triggered);

    @Query("SELECT a FROM Alert a WHERE (a.metricsLookup.name LIKE %:metricName% AND a.operator = :operator AND a.threshold LIKE %:threshold%)" +
            " OR a.metricsLookup.name LIKE %:fullRule% OR a.operator = :fullRule OR  a.threshold LIKE %:fullRule%")
    Page<Alert> findAllByRule(Pageable pageable, @Param("metricName") String metricName, @Param("operator") String operator,
                              @Param("threshold") String threshold, @Param("fullRule") String fullRule);
}
