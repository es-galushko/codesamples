package com.effectivesoft.usermanagement.entity;

public enum AuditEventType {
    USER_LOCKOUT("User lockout", "User [{0}] locked out after [{1}] consecutive failed logins"),
    PASSWORD_RESET("Password reset", ""),
    USER_DELETE("User delete", ""),
    JOB_FAILURE("Job failure", "");

    private String eventName;
    private String auditMessage;

    AuditEventType(String eventName, String auditMessage) {
        this.eventName = eventName;
        this.auditMessage = auditMessage;
    }

    public String getEventName() {
        return eventName;
    }

    public String getAuditMessage() {
        return auditMessage;
    }
}
