package com.effectivesoft.usermanagement.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wls_metrics_lookup")
public class MetricsLookup extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISPLAY_TEXT")
    private String displayText;

    @Column(name = "OBJECT_TYPE")
    private String objectType;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "CUSTOMIZATION_SECTION_NAMBER")
    private Integer customizationSectionNumber;

    @Column(name = "CUSTOMIZATION_DISPLAY_TYPE")
    private Integer customizationDisplayType;

    @Column(name = "CUSTOMIZATION_GROUP_NAMBER")
    private Integer customizationGroupNumber;

    @Column(name = "CUSTOMIZATION_GROUP_ORDER")
    private Integer customizationGroupOrder;

    @Column(name = "CUSTOMIZATION_GROUP_NAME")
    private Integer customizationGroupName;

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

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCustomizationSectionNumber() {
        return customizationSectionNumber;
    }

    public void setCustomizationSectionNumber(Integer customizationSectionNumber) {
        this.customizationSectionNumber = customizationSectionNumber;
    }

    public Integer getCustomizationDisplayType() {
        return customizationDisplayType;
    }

    public void setCustomizationDisplayType(Integer customizationDisplayType) {
        this.customizationDisplayType = customizationDisplayType;
    }

    public Integer getCustomizationGroupNumber() {
        return customizationGroupNumber;
    }

    public void setCustomizationGroupNumber(Integer customizationGroupNumber) {
        this.customizationGroupNumber = customizationGroupNumber;
    }

    public Integer getCustomizationGroupOrder() {
        return customizationGroupOrder;
    }

    public void setCustomizationGroupOrder(Integer customizationGroupOrder) {
        this.customizationGroupOrder = customizationGroupOrder;
    }

    public Integer getCustomizationGroupName() {
        return customizationGroupName;
    }

    public void setCustomizationGroupName(Integer customizationGroupName) {
        this.customizationGroupName = customizationGroupName;
    }
}