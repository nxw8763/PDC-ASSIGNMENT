package dto;

public class TicketCardDTO {

    private final int ticketId;

    private final String title;

    private final String category;

    private final String priority;

    private final String status;

    private final String assignedTechnician;

    public TicketCardDTO(
            int ticketId,
            String title,
            String category,
            String priority,
            String status,
            String assignedTechnician
    ) {

        this.ticketId = ticketId;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.assignedTechnician = assignedTechnician;
    }

    public int getTicketId() {
        return ticketId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public String getAssignedTechnician() {
        return assignedTechnician;
    }
}