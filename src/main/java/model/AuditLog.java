package model;

import java.time.LocalDateTime;

import model.enums.AuditAction;
import model.enums.AuditEntity;

public class AuditLog {

    private final int auditId;
    private final int userId;

    private final AuditAction action;
    private final AuditEntity entity;

    private final Integer entityId;

    private final String details;

    private final LocalDateTime timestamp;

    public AuditLog(
            int auditId,
            int userId,
            AuditAction action,
            AuditEntity entity,
            Integer entityId,
            String details,
            LocalDateTime timestamp) {

        this.auditId = auditId;
        this.userId = userId;
        this.action = action;
        this.entity = entity;
        this.entityId = entityId;
        this.details = details;
        this.timestamp = timestamp;
    }

    public int getAuditId() {
        return auditId;
    }

    public int getUserId() {
        return userId;
    }

    public AuditAction getAction() {
        return action;
    }

    public AuditEntity getEntity() {
        return entity;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public String getDetails() {
        return details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}