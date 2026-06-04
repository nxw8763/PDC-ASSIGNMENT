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

    private int assignedTechnicianID;
    private String assignedTechnicianEmail;
    
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

    public void setAssignedTechnicianID(int techID) {
        this.assignedTechnicianID = techID;
    }
    
    public void setAssignedTechnicianEmail(String email) {
    	this.assignedTechnicianEmail = email; 
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
    public void setTicketID(int id) {
    	this.ticketID = id;
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
    
    public int getAssignedTechnicianID() {
    	return this.assignedTechnicianID;
    }
    
    public String getAssignedTechnicianEmail() {
    	return this.assignedTechnicianEmail;
    }

}