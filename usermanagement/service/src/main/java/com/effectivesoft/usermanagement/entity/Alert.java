package com.effectivesoft.usermanagement.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "alerts")
public class Alert extends AbstractEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    //TODO - remove for new database version
    @Column(name = "OBJECT_ID")
    private Integer objectId;

    @ManyToOne
    @JoinColumn(name = "METRIC_ID", referencedColumnName = "ID")
    private MetricsLookup metricsLookup;

    @Column(name = "TRIGGERED")
    private Boolean triggered;

    @Column(name = "THRESHOLD")
    private String threshold;

    @Column(name = "OPERATOR")
    private String operator;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public MetricsLookup getMetricsLookup() {
        return metricsLookup;
    }

    public void setMetricsLookup(MetricsLookup metricsLookup) {
        this.metricsLookup = metricsLookup;
    }

    public Boolean getTriggered() {
        return triggered;
    }

    public void setTriggered(Boolean triggered) {
        this.triggered = triggered;
    }

    public String getThreshold() {
        return threshold;
    }

    public void setThreshold(String threshold) {
        this.threshold = threshold;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
