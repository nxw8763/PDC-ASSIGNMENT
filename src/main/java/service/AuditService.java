package service;

import dao.AuditDAO;
import model.enums.AuditAction;
import model.enums.AuditEntity;
import model.users.Admin;
import model.AuditLog;

import java.util.List;

public class AuditService {

    public static void addAuditLog(
            int userId,
            AuditAction action,
            AuditEntity entity,
            Integer entityId,
            String details) {

        AuditDAO.addAuditLog(
                userId,
                action,
                entity,
                entityId,
                details
        );
    }

    public static List<AuditLog> getAllAuditLogs(Admin admin) {
    	if (admin == null) {
        	AuditService.addAuditLog(0, AuditAction.VIOLATION, AuditEntity.USER , 0, "someone violated access policy for auditlog service using null admin value");
            throw new IllegalArgumentException("Admin cannot be null.");
        }
        return AuditDAO.getAllAuditLogs();
    }
}