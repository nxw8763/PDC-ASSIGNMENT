package dto;

import java.util.Map;

public class OverviewDTO {

    private final Map<String, Integer> statusCounts;

    private final Map<String, Integer> priorityCounts;

    private final Map<String, Integer> categoryCounts;

    private final Map<String, Integer> technicianWorkload;

    private final Map<String, Integer> ticketTrend;

    public OverviewDTO(
            Map<String, Integer> statusCounts,
            Map<String, Integer> priorityCounts,
            Map<String, Integer> categoryCounts,
            Map<String, Integer> technicianWorkload,
            Map<String, Integer> ticketTrend
    ) {
        this.statusCounts = statusCounts;
        this.priorityCounts = priorityCounts;
        this.categoryCounts = categoryCounts;
        this.technicianWorkload = technicianWorkload;
        this.ticketTrend = ticketTrend;
    }

    public Map<String, Integer> getStatusCounts() {
        return statusCounts;
    }

    public Map<String, Integer> getPriorityCounts() {
        return priorityCounts;
    }

    public Map<String, Integer> getCategoryCounts() {
        return categoryCounts;
    }

    public Map<String, Integer> getTechnicianWorkload() {
        return technicianWorkload;
    }

    public Map<String, Integer> getTicketTrend() {
        return ticketTrend;
    }
}