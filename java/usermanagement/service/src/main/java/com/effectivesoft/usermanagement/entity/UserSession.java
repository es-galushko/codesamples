package com.effectivesoft.usermanagement.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_sessions")
public class UserSession extends AbstractEntity{
    @Column(name = "TOKEN")
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EXPIRATION")
    private Date expiration;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_ACCESSED_TIME")
    private Date lastAccessedTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    @Column(name = "LAST_ACCESSED_IP")
    private String lastAccessedIp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Date getLastAccessedTime() {
        return lastAccessedTime;
    }

    public void setLastAccessedTime(Date lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastAccessedIp() {
        return lastAccessedIp;
    }

    public void setLastAccessedIp(String lastAccessedIp) {
        this.lastAccessedIp = lastAccessedIp;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
