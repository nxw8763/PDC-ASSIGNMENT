package dto;

public class AuditLogDTO {

    private final int auditId;
    private final int userId;
    private final String action;
    private final String entity;
    private final int entityId;
    private final String details;
    private final String timestamp;

    public AuditLogDTO(
            int auditId,
            int userId,
            String action,
            String entity,
            int entityId,
            String details,
            String timestamp) {

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

    public String getAction() {
        return action;
    }

    public String getEntity() {
        return entity;
    }

    public int getEntityId() {
        return entityId;
    }

    public String getDetails() {
        return details;
    }

    public String getTimestamp() {
        return timestamp;
    }
}