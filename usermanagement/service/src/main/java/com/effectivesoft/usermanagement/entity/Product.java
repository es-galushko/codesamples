package com.effectivesoft.usermanagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
public class Product extends AbstractEntity {

    @Column(name = "NAME")
    private String name;

    @Column(name = "SHORT_NAME")
    private String shortName;

    @Column(name = "RELEASE_CODE")
    private String releaseCode;

    @Column(name = "VERSION_CODE")
    private String versionCode;

    @Column(name = "ENABLED")
    private Boolean enabled;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<RolePermission> rolePermissions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Alert> alerts;

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getReleaseCode() {
        return releaseCode;
    }

    public void setReleaseCode(String releaseCode) {
        this.releaseCode = releaseCode;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<RolePermission> getRolePermissions() {
        if (rolePermissions == null){
            rolePermissions = new ArrayList<RolePermission>();
        }
        return rolePermissions;
    }

    public void setRolePermissions(List<RolePermission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public List<Alert> getAlerts() {
        if (alerts == null){
            alerts = new ArrayList<Alert>();
        }
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
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
