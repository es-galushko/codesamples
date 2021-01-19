package com.effectivesoft.usermanagement.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends AbstractEntity {

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMAIL")
    private String email;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LAST_LOGON")
    private Date lastLogon;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "INVALID_LOGIN_COUNT")
    private Integer invalidLoginCount;

    @Column(name = "LOCKED")
    private Boolean locked;

    @Column(name = "ADMIN")
    private Boolean admin;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

    @Column(name = "UPDATED_BY")
    private String updatedBy;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<UserSession> userSessions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<UserRole> userRoles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getLastLogon() {
        return lastLogon;
    }

    public void setLastLogon(Date lastLogon) {
        this.lastLogon = lastLogon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInvalidLoginCount() {
        return invalidLoginCount;
    }

    public void setInvalidLoginCount(Integer invalidLoginCount) {
        this.invalidLoginCount = invalidLoginCount;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public List<UserSession> getUserSessions() {
        if (userSessions == null){
            userSessions = new ArrayList<UserSession>();
        }
        return userSessions;
    }

    public void setUserSessions(List<UserSession> userSessions) {
        this.userSessions = userSessions;
    }

    public List<UserRole> getUserRoles() {
        if (userRoles == null){
            userRoles = new ArrayList<UserRole>();
        }
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        User user = (User) o;

        return username.equals(user.username);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }
}
