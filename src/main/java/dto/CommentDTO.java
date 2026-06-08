package dto;

public class CommentDTO {

    private final String title;

    private final String description;

    private final String createdBy;

    private final String createdDate;

    public CommentDTO(
            String title,
            String description,
            String createdBy,
            String createdDate) {

        this.title = title;
        this.description = description;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}