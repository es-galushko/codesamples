package com.effectivesoft.usermanagement.entity.metric;

import com.effectivesoft.usermanagement.entity.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public abstract class AbstractMetricLookup extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "DISPLAY_TEXT")
    private String displayText;

    @Column(name = "STRING")
    private Boolean string;

    @Column(name = "COLLECTION_ENABLED")
    private Boolean collectionEnabled;

    @Column(name = "SECTION")
    private Integer section;

    @Column(name = "TEMPLATE_TYPE")
    private String templateType;

    @Column(name = "TEMPLATE_GROUP")
    private Integer templateGroup;

    @Column(name = "TEMPLATE_ORDER")
    private Integer templateOrder;

    @Column(name = "TEMPLATE_GROUP_NAME")
    private String templateGroupName;

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

    public Boolean getString() {
        return string;
    }

    public void setString(Boolean string) {
        this.string = string;
    }

    public Boolean getCollectionEnabled() {
        return collectionEnabled;
    }

    public void setCollectionEnabled(Boolean collectionEnabled) {
        this.collectionEnabled = collectionEnabled;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public Integer getTemplateGroup() {
        return templateGroup;
    }

    public void setTemplateGroup(Integer templateGroup) {
        this.templateGroup = templateGroup;
    }

    public Integer getTemplateOrder() {
        return templateOrder;
    }

    public void setTemplateOrder(Integer templateOrder) {
        this.templateOrder = templateOrder;
    }

    public String getTemplateGroupName() {
        return templateGroupName;
    }

    public void setTemplateGroupName(String templateGroupName) {
        this.templateGroupName = templateGroupName;
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
