package controller;

import dto.AuditLogDTO;
import gui.admin.AuditLogView;
import model.AuditLog;
import model.users.Admin;
import service.AuditService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditController {

    public void loadAuditLogs(
            Admin admin,
            AuditLogView view) {

        try {

            List<AuditLog> logs =
                    AuditService.getAllAuditLogs(
                            admin
                    );

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd HH:mm:ss"
                    );

            List<AuditLogDTO> dtoList =
                    new ArrayList<>();

            for (AuditLog log : logs) {

                dtoList.add(
                        new AuditLogDTO(
                                log.getAuditId(),
                                log.getUserId(),
                                log.getAction().toString(),
                                log.getEntity().toString(),
                                log.getEntityId(),
                                log.getDetails(),
                                log.getTimestamp()
                                        .format(formatter)
                        )
                );
            }

            view.displayAuditLogs(
                    dtoList
            );

        } catch (Exception ex) {

            view.showError(
                    ex.getMessage()
            );
        }
    }
}