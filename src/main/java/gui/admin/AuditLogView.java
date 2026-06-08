package gui.admin;

import dto.AuditLogDTO;

import java.util.List;

public interface AuditLogView {

    void displayAuditLogs(
            List<AuditLogDTO> logs
    );

    void showError(
            String message
    );
}