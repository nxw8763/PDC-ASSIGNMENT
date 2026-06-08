package dto;

import java.util.List;

public class TicketDetailsDTO {

    private final int ticketId;

    private final String title;

    private final String category;

    private final String priority;

    private final String status;

    private final String assignedTechnician;

    private final String createdDate;

    private final String description;

    private final List<CommentDTO> comments;

    public TicketDetailsDTO(
            int ticketId,
            String title,
            String category,
            String priority,
            String status,
            String assignedTechnician,
            String createdDate,
            String description,
            List<CommentDTO> comments) {

        this.ticketId = ticketId;
        this.title = title;
        this.category = category;
        this.priority = priority;
        this.status = status;
        this.assignedTechnician = assignedTechnician;
        this.createdDate = createdDate;
        this.description = description;
        this.comments = comments;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public String getDescription() {
        return description;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }
}