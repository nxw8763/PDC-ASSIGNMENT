package service;

import dao.AuditDAO;
import model.enums.AuditAction;
import model.enums.AuditEntity;
import model.Admin;
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
        return AuditDAO.getAllAuditLogs();
    }
}