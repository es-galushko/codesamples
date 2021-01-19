package com.effectivesoft.usermanagement.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wls_audit")
public class Audit extends AbstractEntity {

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EVENT_NAME")
    private String eventName;

    @Column(name = "EVENT_DESCRIPTION")
    private String eventDescription;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    private Date createdAt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
