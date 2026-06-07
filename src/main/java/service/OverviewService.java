package service;

import dao.OverviewDAO;
import model.LabelCount;
import model.enums.AuditAction;
import model.enums.AuditEntity;
import model.users.Admin;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OverviewService {

    private final OverviewDAO overviewDAO = new OverviewDAO();

    private void validateAdmin(Admin admin) {
        if (admin == null) {
            AuditService.addAuditLog(
                    0,
                    AuditAction.VIOLATION,
                    AuditEntity.USER,
                    0,
                    "null admin access to overview service"
            );
            throw new IllegalArgumentException("Admin cannot be null.");
        }
    }

    private Map<String, Integer> toMap(List<LabelCount> list) {

        Map<String, Integer> map = new LinkedHashMap<>();

        for (LabelCount lc : list) {
            map.put(lc.label(), lc.count());
        }

        return map;
    }

    public Map<String, Integer> getTicketTrend(Admin admin) {
        validateAdmin(admin);
        return toMap(overviewDAO.getTicketTrend());
    }

    public Map<String, Integer> getPriorityCounts(Admin admin) {
        validateAdmin(admin);
        return toMap(overviewDAO.getPriorityCounts());
    }

    public Map<String, Integer> getStatusCounts(Admin admin) {
        validateAdmin(admin);
        return toMap(overviewDAO.getStatusCounts());
    }

    public Map<String, Integer> getCategoryCounts(Admin admin) {
        validateAdmin(admin);
        return toMap(overviewDAO.getCategoryCounts());
    }

    public Map<String, Integer> getTechnicianWorkload(Admin admin) {
        validateAdmin(admin);
        return toMap(overviewDAO.getTechnicianWorkload());
    }
}