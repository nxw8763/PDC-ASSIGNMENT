package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Ticket {

    private int ticketID;
    private String title;
    private String description;
    private String category;
    private Priority priority;
    private Status status;
    private LocalDateTime createdDate;

    private int createdByUserID;

    private String assignedTechnician = "";

    private List<Comment> comments;

    public Ticket(int ticketID, String title, String description,
            String category, Priority priority,
            int createdByUserID, Status status,
            LocalDateTime createdDate) {

  this.ticketID = ticketID;
  this.title = title;
  this.description = description;
  this.category = category;
  this.priority = priority;
  this.createdByUserID = createdByUserID;
  this.status = status;
  this.createdDate = createdDate;
  this.comments = new ArrayList<>();
}

    /* METHODS */

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public void assignTechnician(String tech) {
        this.assignedTechnician = tech;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    /* GETTERS */

    public int getTicketID() {
        return ticketID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }
    public int getCreatedByUserID() {
        return createdByUserID;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public List<Comment> getComments() {
    	return this.comments;
    }
    
    public String getCategory() {
        return category;
    }
    
    public String getAssignedTechnician() {
    	return this.assignedTechnician;
    }
    
    public void printTicket() {

        System.out.println("=================================================");
        System.out.println("                 SUPPORT TICKET                  ");
        System.out.println("=================================================");

        System.out.printf("%-18s : %d\n", "Ticket ID", ticketID);
        System.out.printf("%-18s : %s\n", "Title", title);
        System.out.printf("%-18s : %s\n", "Description", description);
        System.out.printf("%-18s : %s\n", "Category", category);
        System.out.printf("%-18s : %s\n", "Priority", priority);
        System.out.printf("%-18s : %s\n", "Status", status);
        System.out.printf("%-18s : %d\n", "Created By", createdByUserID);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        System.out.printf("%-18s : %s\n", "Created Date", createdDate.format(formatter));

        if (assignedTechnician != null) {
            System.out.printf("%-18s : %s\n", "Technician", assignedTechnician);
        } else {
            System.out.printf("%-18s : %s\n", "Technician", "Unassigned");
        }

        System.out.println("-------------------------------------------------");

        if (comments.isEmpty()) {
            System.out.println("Comments: None");
        } else {
            System.out.println("Comments:");
            for (Comment c : comments) {
                System.out.println(c.getTitle() + " - " + c.getDescription() + " - " +c.getCreatedByUser() + " - " + c.getCreatedDate().format(formatter));
            }
        }

        System.out.println("=================================================");
    }
}