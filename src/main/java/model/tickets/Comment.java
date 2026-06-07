package model.tickets;

import java.time.LocalDateTime;

public class Comment {

    private String title;
    private String description;
    private String createdByUser;
    private LocalDateTime createdDate;

    public Comment(String title, String description, String createdBy) {
        this.title = title;
        this.description = description;
        this.createdByUser = createdBy;
        this.createdDate = LocalDateTime.now();
    }
    
    public Comment(String title, String description, String createdBy, LocalDateTime createdDate) {
        this.title = title;
        this.description = description;
        this.createdByUser = createdBy;
        this.createdDate = createdDate;
    }
    
    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getCreatedDate() {
    	return this.createdDate;
    }

    public String getCreatedByUser() {
        return createdByUser;
    }

}